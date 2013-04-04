package org.morningcoffee.powerteam.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.google.gson.*;

public class DBLogger {

	private String dbURL = "jdbc:derby:" + System.getProperty("user.dir")
			+ "/db;create=true";
	private String sqlPath = System.getProperty("user.dir") + "/db/dbcreate.sql";
	private Connection conn = null;
	private Statement stmt = null;

	public void createConnection() throws FileNotFoundException {
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			conn = DriverManager.getConnection(dbURL, "user", "user");
		} catch (IllegalAccessException | InstantiationException
				| ClassNotFoundException | SQLException e) {
			System.out.println("\nCannot connect to DB\n");
		}

		String s = new String();
		StringBuffer sb = new StringBuffer();

		try {
			FileReader fr = new FileReader(new File(sqlPath));
			BufferedReader br = new BufferedReader(fr);

			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			br.close();

			String[] inst = sb.toString().split(";");

			stmt = conn.createStatement();

			for (int i = 0; i < inst.length; i++)
				if (!inst[i].trim().equals("")) {
					stmt.executeUpdate(inst[i]);
					System.out.println(">>" + inst[i]);
				}

		} catch (SQLException | IOException e) {
			System.err.println("Failed to Execute " + sqlPath
					+ ". The error is" + e.getMessage());
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

		ResultSet rs = null;
		String request = "SELECT APP.USERS.USER_NAME, APP.CLIENTLOGS.HASH, APP.CLIENTLOGS.DATE, "
				+ "MAX(APP.PLUGINLOGS.END_TIME), APP.PLUGINLOGS.TEST_RESULT "
				+ "FROM APP.CLIENTLOGS LEFT OUTER JOIN APP.PLUGINLOGS "
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
				map.put("push_time", rs.getString(3));
				map.put("test_time", rs.getString(4));
				map.put("test_result", rs.getString(5));
				tableData.add(map);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tableData;
	}
}
