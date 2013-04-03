package org.morningcoffee.powerteam.server;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.google.gson.*;


public class DBLogger {

	private String dbURL = "jdbc:derby:" + System.getProperty("user.dir")
			+ "/db;create=true";
	private Connection conn = null;
	private Statement stmt = null;

	public void createConnection() {
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			conn = DriverManager.getConnection(dbURL, "user", "user");
		} catch (IllegalAccessException | InstantiationException
				| ClassNotFoundException | SQLException e) {
			System.out.println("\nCannot connect to DB\n");
			System.exit(-1);
		}
	}

	public void closeConnection() {
		try {
			if (stmt != null)
				stmt.close();
			if (conn != null) {
				DriverManager.getConnection(dbURL + ";shutdown=true");
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("\nCannot close connection to DB\n");
			System.exit(-2);
		}
	}

	public void addLog(String data) {
		RequestModel rq = new Gson().fromJson(data, RequestModel.class);
		String sqlReq = "";
		if (rq.type.equals("plugin"))
			sqlReq = String
					.format("INSERT INTO APP.PLUGINLOGS (start_time, end_time, test_result, user_id) values "
							+ "(%d, %d, '%s', (SELECT USER_ID FROM APP.USERS WHERE USER_NAME = '%s'))",
							rq.startTime, rq.endTime, rq.testResult,
							rq.userName);
		else {
			try {
				Date date = new SimpleDateFormat("EEE MMM dd H:m:s yyyy",
						Locale.ENGLISH).parse(rq.date);
				sqlReq = String
						.format("INSERT INTO APP.CLIENTLOGS (hash, date, user_id) values ('%s', %d, "
								+ "(SELECT USER_ID FROM APP.USERS WHERE USER_NAME = '%s'))",
								rq.hash, date.getTime(), rq.userName);
			} catch (ParseException e) {
				System.out.println("\nCannot convert date: " + rq.date + "\n");
			}
		}

		try {
			stmt = conn.createStatement();
			stmt.execute(sqlReq);
			stmt.close();
		} catch (SQLException e) {
			System.out.println("\nCannot write log to DB:\n" + sqlReq + "\n");
		}
	}

	public List<HashMap<String, String>> getLogs() {
		List<HashMap<String, String>> tableData = new ArrayList<HashMap<String, String>>();
		DateFormat df = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss");
		
		ResultSet rs = null;
		/*String request = "SELECT APP.USERS.USER_NAME, APP.CLIENTLOGS.HASH, APP.CLIENTLOGS.DATE, " 
				+ "APP.PLUGINLOGS.END_TIME, APP.PLUGINLOGS.TEST_RESULT "
				+ "FROM APP.CLIENTLOGS LEFT OUTER JOIN APP.PLUGINLOGS "
				+ "ON (APP.CLIENTLOGS.DATE - APP.PLUGINLOGS.END_TIME) <= 300000 AND " 
				+ "(APP.CLIENTLOGS.DATE - APP.PLUGINLOGS.END_TIME) > 0 JOIN APP.USERS "
				+ "ON APP.CLIENTLOGS.USER_ID = APP.USERS.USER_ID ORDER BY APP.CLIENTLOGS.DATE DESC";*/
		
		String request = "SELECT APP.USERS.USER_NAME, APP.CLIENTLOGS.HASH, APP.CLIENTLOGS.DATE, " 
				+ "MAX(APP.PLUGINLOGS.END_TIME) AS EDATE, APP.PLUGINLOGS.TEST_RESULT "
				+ "FROM APP.CLIENTLOGS LEFT JOIN APP.PLUGINLOGS "
				+ "ON APP.CLIENTLOGS.USER_ID = APP.PLUGINLOGS.USER_ID AND " 
				+ "(APP.CLIENTLOGS.DATE - APP.PLUGINLOGS.END_TIME) <= 300000 AND " 
				+ "(APP.CLIENTLOGS.DATE - APP.PLUGINLOGS.END_TIME) > 0 JOIN APP.USERS "
				+ "ON APP.CLIENTLOGS.USER_ID = APP.USERS.USER_ID "
				+ "GROUP BY APP.USERS.USER_NAME, APP.CLIENTLOGS.HASH, APP.CLIENTLOGS.DATE, "
				+ "APP.PLUGINLOGS.TEST_RESULT ORDER BY APP.CLIENTLOGS.DATE DESC";

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(request);

			while (rs.next()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("user_name", rs.getString(1));
				map.put("push_hash", rs.getString(2));
				//map.put("push_time", df.format(new Date(rs.getLong(3))));
				map.put("push_time", rs.getString(3));
				if(!rs.getBoolean(4))
					map.put("test_time", " - ");
				else map.put("test_time", rs.getString(4));
				if(!rs.getBoolean(5))
					map.put("test_result", " - ");
				else map.put("test_result", rs.getString(5));
				tableData.add(map);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tableData;
	}
}
