package net.wigoftime.open_komodo.sql;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.Config;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.SQLInfo;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SQLCard {
    public static BasicDataSource dataSource = new BasicDataSource();
    private static SQLInfo info;


    public static void setup() {
        info = Config.getSQLInfo();


        if (!info.enabled) {
            PrintConsole.print(ChatColor.DARK_RED+"OPEN KOMODO NO LONGER SUPPORTS NON-MYSQL DATABASE SETUP. " +
                    "PLEASE SETUP MYSQL SERVER TO CONTINUE.");
            Bukkit.getServer().shutdown();
            return;
        }

        String typeExtention;
        if (info.type.equalsIgnoreCase("mysql")) typeExtention = "mysql";
        else if (info.type.equalsIgnoreCase("MariaDB")) typeExtention = "mariadb";
        else typeExtention = "";

        dataSource.setUsername(info.user);
        dataSource.setPassword(info.password);
        dataSource.setUrl("jdbc:"+typeExtention+"://"+info.host+"/"+info.database);
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);

        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException exception) {
            PrintConsole.print(ChatColor.DARK_RED+"MYSQL SERVER IS NOT SETUP CORRECTLY, SHUTTING OFF SERVER.");
            exception.printStackTrace();
            PrintConsole.print(ChatColor.DARK_RED+"MYSQL SERVER IS NOT SETUP CORRECTLY, SHUTTING OFF SERVER.");
            Bukkit.getServer().shutdown();
        } finally {
            if (connection == null) try { connection.close();} catch (SQLException exception) {};
        }
    }

    public static enum SQLCardType {GET, SET};

    public final String sql;
    public final SQLCard.SQLCardType type;
    public final List<Object> sqlValues;

    public SQLCard(SQLCode.SQLCodeType sql, SQLCard.SQLCardType type, List<Object> formatString, List<Object> sqlValues) {
        this.sql = String.format(SQLCode.getSQLCode(sql).toString(), formatString.toArray());
        this.type = type;
        this.sqlValues = sqlValues;
    }

    public List<Object> execute() {
        if (type == SQLCard.SQLCardType.SET) { executeSet(); return null;}
        return executeQuery();
    }

    private void executeSet() {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);

            for (int index = 1; index <= sqlValues.size(); index++)
            statement.setObject(index,sqlValues.get(index-1));

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

            for (int index = 1; index <= sqlValues.size(); index++)
                statement.setObject(index,sqlValues.get(index-1));

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
