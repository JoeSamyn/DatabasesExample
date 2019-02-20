/*
 * Here is a basic setup for using a sqlite database with java and intellij.  There are a few things to make sure you
 * have downloaded.  First you need to grab the sqlite JDBC driver from the link here:
 * https://bitbucket.org/xerial/sqlite-jdbc/downloads/
 * Once this jar file is downloaded, you need to bring it into the project. This is done by going to:
 * File -> Project Structure -> Libraries
 * Then add a new library using the plus icon in the upper right hand corner, and then find the jar file you downloaded
 * and import it.  When it is finished importing click Apply and then Okay.  The rest of the steps are explained below.
 * Feel free to leave any comments if something needs updating
 *
 * Author: Joe Samyn
 * Date Modified: 2.20.19
 * Title: Sqlite Setup Tutorial
 */




package com.joesamyn.DatabasesExample;

import java.sql.*;

public class JdbcMain {

    public static void main(String[] args) throws ClassNotFoundException {
        // This is the sqlite driver class to be used for this database.
        Class.forName("org.sqlite.JDBC");




        // Create a DB connection.  The URL is the path to the database.
        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:contactmgr.db")) {

            // Lets us know we have a connection to the DB.
            System.out.println("Connection to SQLite has been established.");
            //This creates our SQL statement
            Statement statement = connection.createStatement();

            // Create a new DB table
            // First we drop the contact table if it already exists so we do not get a Table Already Exists error.
            statement.executeUpdate("DROP TABLE IF EXISTS contacts");
            // We insert the rows with the names that we want into the table.
            statement.executeUpdate("CREATE TABLE contacts (id INTEGER PRIMARY KEY, firstname STRING, lastname STRING, email STRING, phone INT(10))");

            // We create a new contact object and then save it using the save method we created
            Contact c = new Contact("Jackie", "Samyn", "jackie@gmail.com", 2197767123L);
            save(c, statement);

            //Insert a couple rows into the table.
            // Just inserting two rows of data here for printing purposes.
            statement.executeUpdate("INSERT INTO contacts (firstname, lastname, email, phone) VALUES ('Joe', 'Samyn','js@gmail.com', 2197767123)");
            statement.executeUpdate("INSERT INTO contacts (firstname, lastname, email, phone) VALUES ('Jimmy', 'Samyn','jimmyS@gmail.com', 2197757055)");

            //Fetch all of the rows from the contacts table.
            // We set it equal to a result set so we can iterate over the results and find what we want.
            ResultSet rs = statement.executeQuery("SELECT * FROM contacts");

            //Display the results
            while(rs.next()){
                // We place the id field into an integer variable
                int id = rs.getInt("id");
                // The firstname and lastname fields into a string variable
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");

                // Then print them out in the format specified.
                System.out.printf("%s %s %d%n", firstname, lastname, id);


            }


        } catch (SQLException e) {
            System.err.printf("There was a problem connecting to the DB: %s%n", e.getMessage());
        }
    }

    // Since we already have a try catch block in the main method, we just add a throws SQLException to let the main method
    // handle any issues.
    public static void save(Contact contact, Statement statement) throws SQLException {

        // Compose the query
        // We make our query a string with placeholders
        String sql = "INSERT INTO contacts (firstname, lastname, email, phone) VALUES ('%s','%s','%s',%d)";
        // Then using String.format we insert values from our contact object into the sql string.
        sql = String.format(sql, contact.getFirstName(), contact.getLastName(), contact.getEmail(), contact.getPhone());

        // Execute Query
        // This is what updates the table with our new information from the contact class that was place into the sql string.
        statement.executeUpdate(sql);

    }

}
