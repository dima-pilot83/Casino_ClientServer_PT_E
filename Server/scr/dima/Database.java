package dima;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    public void addToDb(String name, int balanceVersion, int balance) {

        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error.");
            e.printStackTrace();
            System.exit(1);
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:hsqldb:file:D:/dbname", "SA", "");
        } catch (SQLException e) {
            System.err.println("Error.");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            Statement statement = connection.createStatement();

            String query = "CREATE TABLE player (id IDENTITY , username VARCHAR(32), balance_version INT, balance INT)";
            try {
                statement.executeUpdate(query);
            } catch (SQLException e) {
            }
            statement.close();
            statement = connection.createStatement();

            try {
                PreparedStatement pstmt = connection
                        .prepareStatement("INSERT INTO player (username,balance_version,balance) VALUES (?,?,?)");
                pstmt.setString(1, name);
                pstmt.setInt(2, balanceVersion);
                pstmt.setInt(3, balance);
                pstmt.executeUpdate();
            } catch (Exception ex) {
                System.out.println("Got error: " + ex.getMessage());
            }
            statement.close();

            statement = connection.createStatement();
            query = "SHUTDOWN";
            statement.execute(query);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDb(String name, int balance) {

        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error.");
            e.printStackTrace();
            System.exit(1);
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:hsqldb:file:D:/dbname", "SA", "");
        } catch (SQLException e) {
            System.err.println("Error.");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE player (id IDENTITY , username VARCHAR(32), balance_version INT, balance INT)";
            try {
                statement.executeUpdate(query);
            } catch (SQLException e) {
            }
            statement.close();

            try {
                statement = connection.createStatement();
                PreparedStatement pstmt = connection.prepareStatement("UPDATE player SET balance=? WHERE username = ?");
                pstmt.setInt(1, balance);
                pstmt.setString(2, name);
                pstmt.executeUpdate();
            } catch (Exception ex) {
                System.out.println("Got error: " + ex.getMessage());
            }
            statement.close();

            statement = connection.createStatement();
            query = "SHUTDOWN";
            statement.execute(query);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readDb() {

        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error.");
            e.printStackTrace();
            System.exit(1);
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:hsqldb:file:D:/dbname", "SA", "");
        } catch (SQLException e) {
            System.err.println("Error.");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE player (id IDENTITY , username VARCHAR(32), balance_version INT, balance INT)";
            try {
                statement.executeUpdate(query);
            } catch (SQLException e) {

            }
            statement.close();

            statement = connection.createStatement();
            query = "SELECT id, username, balance_version, balance FROM player";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2) + " " + resultSet.getInt(3) + " "
                        + resultSet.getInt(4));
            }
            statement.close();
            statement = connection.createStatement();
            query = "SHUTDOWN";
            statement.execute(query);
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
