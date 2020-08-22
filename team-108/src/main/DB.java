package main;


import java.sql.*;
import java.util.ArrayList;

import com.mysql.jdbc.StringUtils;
import org.json.simple.JSONObject;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DB implements DB_params{

    private static com.mysql.jdbc.Driver driver;
    private static Connection connection;

    private static Context initContext;
    private static Context envContext;
    private static DataSource ds;

    private static ResultSet resultSet;
    private static PreparedStatement statement;

    private static void connect() throws SQLException {
        driver = new com.mysql.jdbc.Driver();

//        connection = DriverManager.getConnection(
//                "jdbc:" + DB_params.db_type
//                        + ":///" + DB_params.db_name
//                        + "?autoReconnect=true&useSSL=false",
//                DB_params.username,
//                DB_params.password);

        try{ // project 5: using connection pooling instead
            initContext = new InitialContext();
            envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("jdbc/bikedb");
            connection = ds.getConnection();
        } catch (java.lang.Exception ex) {
            System.out.println("DB.connect throws lang exception");
        }
        if (connection == null)
            System.out.println("connection is null");

        if (connection == null) {
            throw new SQLException("DB connect failed");
        }

    }

    private static void connectMaster() throws SQLException {
        driver = new com.mysql.jdbc.Driver();
        try{ // project 5: using connection pooling instead
            initContext = new InitialContext();
            envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("jdbc/bikedbMaster");
            connection = ds.getConnection();
        } catch (java.lang.Exception ex) {
            System.out.println("DB.connect throws lang exception");
        }
        if (connection == null)
            System.out.println("connection is null");

        if (connection == null) {
            throw new SQLException("DB connect failed");
        }
    }

    private static void closeAll() {
        try { if (resultSet != null) resultSet.close(); } catch (Exception e) {};
        try { if (statement != null) statement.close(); } catch (Exception e) {};
        try { if (connection != null) connection.close(); } catch (Exception e) {};
    }

    private static void rawAccess(String query, ArrayList<String> paras, boolean master) throws SQLException {
        /*
         * param: query
         * return: result set */
        if (master) {
            connectMaster();
        } else {
            connect();
        }
        statement = connection.prepareStatement(query);
        for (int i = 0; i < paras.size(); ++i) {
            if (StringUtils.isStrictlyNumeric(paras.get(i))) {
                statement.setInt(i+1, Integer.parseInt(paras.get(i)));
            } else {
                statement.setString(i+1, paras.get(i));
            }
        }
        resultSet = statement.executeQuery();
    }

    private static void rawAccess(PreparedStatement query) throws SQLException {
        /*
         * param: query
         * return: result set */
        connect();
        resultSet = statement.executeQuery();
    }

    public static void update(String query) {
        /*
         * param: query */
        try {
//            connect(); // update master repl
            connectMaster();
            statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    public static void update(String query, ArrayList<String> paras) {
        /*
         * param: query
         * */
        try {
//            connect(); // update master repl
            connectMaster();
            statement = connection.prepareStatement(query);
            for (int i = 0; i < paras.size(); ++i) {
                statement.setString(i+1, paras.get(i));
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    public static JSONObject listAccess(String query, boolean master) {
        return listAccess(query, new ArrayList<String>(), master);
    }

    public static JSONObject listAccess(String query) {
        return listAccess(query, new ArrayList<String>(), false);
    }

//    public static JSONObject listAccess(PreparedStatement query) {
//        try {
//            rawAccess(query);
//            ResultSetMetaData meta = resultSet.getMetaData();
//            JSONObject json = new JSONObject();
//            for (int r = 0; resultSet.next(); ++r) {
//                JSONObject row = new JSONObject();
//                for (int c = 1; c <= meta.getColumnCount(); ++c) {
//                    String columnName = meta.getColumnName(c);
//                    row.put(columnName, resultSet.getString(columnName));
//                }
//                json.put(r, row);
//            }
//            return json;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            closeAll();
//        }
//        return null;
//    }

    public static JSONObject listAccess(String query, ArrayList<String> paras, boolean master) {
        return listAccess(query, paras, -1, master);
    }

    public static JSONObject listAccess(String query, ArrayList<String> paras) {
        return listAccess(query, paras, -1, false);
    }

    public static JSONObject listAccess(String query, ArrayList<String> paras, int maxCount, boolean master) {

        boolean hasMaxCount = maxCount != -1;

        try {
            rawAccess(query, paras, master);
            ResultSetMetaData meta = resultSet.getMetaData();
            JSONObject json = new JSONObject();
            for (int r = 0; resultSet.next() && ((hasMaxCount) ? r < maxCount : true); ++r) {
                JSONObject row = new JSONObject();
                for (int c = 1; c <= meta.getColumnCount(); ++c) {
                    String columnName = meta.getColumnName(c);
                    row.put(columnName, resultSet.getString(columnName));
                }
                json.put(r, row);
            }
            return json;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }

        return null;
    }

}