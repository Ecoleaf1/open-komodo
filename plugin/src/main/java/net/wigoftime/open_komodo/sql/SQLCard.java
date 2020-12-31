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

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import net.wigoftime.open_komodo.config.Config;
import net.wigoftime.open_komodo.objects.SQLInfo;
import net.wigoftime.open_komodo.sql.SQLCode.SQLCodeType;

public class SQLCard {
	private static BasicDataSource ds = new BasicDataSource();
	private static SQLInfo info;
	
	public static void setup() {
		info = Config.getSQLInfo();
		
		if (!info.enabled) return;
		
		ds.setUsername(info.user);
		ds.setPassword(info.password);
		ds.setUrl("jdbc:"+info.url);
		ds.setMinIdle(5);
		ds.setMaxIdle(10);
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
	
	private final int delayAmount = 50;
	
	public List<Object> execute() {
		if (type == SQLCardType.SET) { executeSet(); return null;}
		return executeQuery();
	}
	
	private void executeSet() {
		Connection connection = null;
		PreparedStatement statement = null;
		
		while (true)
		try {
			connection = ds.getConnection();
			statement = connection.prepareStatement(sql);
			
			statement.execute();
			break;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
			setup();
		} finally {
			if (connection != null) try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null) try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	private List<Object> executeQuery() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		
		while (true)
		try {
			connection = ds.getConnection();
			statement = connection.prepareStatement(sql);
			result = statement.executeQuery();
			
			if (!result.next())
				return new ArrayList<Object>(0);
			
			List<Object> listResult = new LinkedList<Object>();
			for (byte index = 1; index <= result.getMetaData().getColumnCount(); index++)
				listResult.add(result.getObject(index));
			
			return listResult;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null) try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null) try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null) try { result.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
}
