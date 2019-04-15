package main;

import java.sql.*;

public class DBManager {
  
	private static final String dbName = "jdbc_test";
    private static final String username = "testuser";   
    private static final String password = "test1234";
    private static final String url = "jdbc:mysql://localhost:3306/" + dbName;  
    
    private static Connection con;
    
    private static String personInsertionQuery(Person person) {
    	return "INSERT INTO persons" +
				"(firstName, lastName, age, street, city, state)" +
				"VALUES(" +
				"'" + person.firstName + "', " +
				"'" + person.lastName + "', " +
				"'" + person.age + "', " +
				"'" + person.street + "', " +
				"'" + person.city + "' " +
				"'" + person.state + "')";
	}

	public static void printResult(ResultSet resultSet) {
		try {
			ResultSetMetaData metaData = resultSet.getMetaData();
			int[] widths = new int[metaData.getColumnCount() + 1];
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				System.out.print(" | " + metaData.getColumnName(i));
				widths[i] = metaData.getColumnName(i).length();
			}
			while(resultSet.next()) {
				System.out.println();
				for (int i = 1; i <= columnCount; i++) {
					String result = resultSet.getString(i);
					if(result == null)
						result = "";
					if(result.length() > widths[i]) {
						result = result.substring(0, widths[i] - 3) + "...";
					} else if (result.length() < widths[i]) {
						String test = new String(new char[widths[i] - result.length()]);
						result += test.replaceAll(""+((char) 0), " ");
					}
					System.out.print(" | " + result);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    public static void main(String[] args) {

    	String dropTableSQLQuery =
				"DROP TABLE IF EXISTS " + dbName + ".persons";

		String createTableSQLQuery =
    			"CREATE TABLE IF NOT EXISTS " + dbName +
    	        ".persons " +
    	        "(ID int NOT NULL AUTO_INCREMENT, " +
    	        "firstName varchar(40) NOT NULL, " +
				"lastName varchar(40) NOT NULL, " +
				"age int NOT NULL, " +
    	        "street varchar(40) NOT NULL, " +
    	        "city varchar(20) NOT NULL, " +
    	        "state char(2), " +
    	        "PRIMARY KEY (ID))";

		String selectSQLQuery =
    			"select id, firstName, lastName, age, street, city, state from persons";

		try {
			con = DriverManager.getConnection(url, username, password);
		} catch (SQLException ex) {
			System.out.println("Failed to create the database connection.");
			ex.printStackTrace();
		}

		Statement stmt = null;

		try {
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

			// CLEANUP
			stmt.execute(dropTableSQLQuery);

			// NEW TABLE
			stmt.execute(createTableSQLQuery);

			// FILL TABLE
			stmt.executeUpdate(personInsertionQuery(new Person(null, "Max", "Mustermann", 25, "Musterstrasse", "Musterstadt", "DE")));
    		stmt.executeUpdate(personInsertionQuery(new Person(null, "Hans", "Peter", 52, "Sackgasse", "Hintertupfingen", "DE")));
    		stmt.executeUpdate(personInsertionQuery(new Person(null, "Angela", "Merkel", 64, "Am Kupfergraben", "Berlin", "DE")));

    		// GET ENTRIES
    		ResultSet rs = stmt.executeQuery(selectSQLQuery);

    		// INSERT NEW ROW
    		rs.moveToInsertRow();
    		rs.updateString(2, "Herr");
    		rs.updateString(3, "Mann");
    		rs.updateInt(4, 32);
    		rs.updateString(5, "Sackgasse");
    		rs.updateString(6, "Hintertupfingen");
    		rs.updateString(7, "DE");
    		rs.insertRow();

    		// PRINT RESULT
    		printResult(rs);
    	} catch(SQLException sqle) {
    		System.out.println("Could not execute query.");
    		sqle.printStackTrace();
    	} finally {
            if (stmt != null) {
            	try {
            		stmt.close();
            	} catch (SQLException sqle) {
            		System.out.println("Could not close statement.");
            		sqle.printStackTrace();
            	}
            }
        }
    	
	}
    
}
