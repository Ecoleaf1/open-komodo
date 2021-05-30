package net.wigoftime.open_komodo.sql;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.wigoftime.open_komodo.etc.PrintConsole;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.apache.tomcat.dbcp.dbcp2.DataSourceConnectionFactory;

import net.wigoftime.open_komodo.config.Config;
import net.wigoftime.open_komodo.objects.SQLInfo;
import net.wigoftime.open_komodo.sql.SQLCode.SQLCodeType;

public class SQLCard {
	private static BasicDataSource dataSource = new BasicDataSource();
	private static SQLInfo info;
	
	public static void setup() {
		info = Config.getSQLInfo();
		
		if (!info.enabled) return;
		
		dataSource.setUsername(info.user);
		dataSource.setPassword(info.password);
		dataSource.setUrl("jdbc:"+info.url);
		dataSource.setMinIdle(5);
		dataSource.setMaxIdle(10);
	}
	
	public static enum SQLCardType {GET, SET};
	
	public final String sql;
	public final SQLCardType type;
	public final List<Object> values;
	
	public SQLCard(SQLCodeType sql, SQLCardType type, List<Object> values) {
		this.sql = String.format(SQLCode.getSQLCode(sql).toString(), values.toArray());
		this.type = type;
		this.values = values;
	}
	
	public List<Object> execute() {
		if (type == SQLCardType.SET) { executeSet(); return null;}
		return executeQuery();
	}
	
	private void executeSet() {
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(sql);
			statement.execute();
		} catch (SQLException exception) {
			exception.printStackTrace();
			return;
		} finally {
			if (connection != null) try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null) try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	private List<Object> executeQuery() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(sql);
			result = statement.executeQuery();
			
			if (!result.next())
				return new ArrayList<Object>(0);

			List<Object> listResult = new LinkedList<Object>();

			PrintConsole.test("SQL | Before do while");
			do {
				PrintConsole.test("SQL | Do While before For loop");
				for (byte index = 1; index <= result.getMetaData().getColumnCount(); index++) {
					listResult.add(result.getObject(index));
				}
				PrintConsole.test("SQL | Do While after For Loop");
				if (!result.next()) break;
			} while (true);

			PrintConsole.test("SQL | After While Loop");

			return listResult;
		} catch (SQLException exception) {
			exception.printStackTrace();
			return null;
		} finally {
			if (statement != null) try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null) try { result.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (connection != null) try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	static protected void disconnect() {
		try {
			dataSource.close();
		} catch (SQLException e) {e.printStackTrace();}
	}
}
