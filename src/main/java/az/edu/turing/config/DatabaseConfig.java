package az.edu.turing.config;

import az.edu.turing.enums.EXCEPTIONS;
import az.edu.turing.exceptions.GeneralExceptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseConfig {
    public static Connection getConnection() {
        Properties prop = new Properties();
        boolean exists = false;

        try (FileInputStream fis = new FileInputStream("src/main/resources/db.properties")) {
            prop.load(fis);
        } catch (IOException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_PROPERTIES_ERROR);
        }
        String url = prop.getProperty("url");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        String dbName = prop.getProperty("name");
        String finalUrl = "jdbc:postgresql://localhost:5432/" + dbName;


        Connection finalConn = null;
        try (
                Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement checkStmt = conn.prepareStatement("SELECT 1 FROM pg_database WHERE datname = ?");
        ) {
            checkStmt.setString(1, dbName);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                try (Statement createStmt = conn.createStatement()) {
                    createStmt.executeUpdate("CREATE DATABASE \"" + dbName + "\"");
                    System.out.println("Database " + dbName + " created.");
                } catch (SQLException e) {
                    throw new GeneralExceptions(EXCEPTIONS.DATABASE_CREATION_ERROR);
                }
            } else {
                exists = true;
            }
            finalConn = DriverManager.getConnection(finalUrl, username, password);
            if (!exists) {
                createTables(finalConn);
                System.out.println("Connected to database successfully");

            }
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }

        return finalConn;
    }

    private static void createTables(Connection con) {
        try (Statement stmt = con.createStatement()) {
            String createFlightSql = """
                    CREATE TABLE IF NOT EXISTS Flights(
                        id serial PRIMARY KEY,
                        creationTime timestamp DEFAULT CURRENT_TIMESTAMP,
                        updatedTime timestamp DEFAULT CURRENT_TIMESTAMP,
                        origin varchar(64) NOT NULL,
                        destination varchar(64) NOT NULL,
                        departureTime timestamp  NOT NULL,
                        arrivalTime timestamp  NOT NULL,
                        seatsNumber integer,
                        availableSeats integer,
                        flightType varchar(32),
                        flightCompany varchar(64)
                    )
                    """;
            stmt.executeUpdate(createFlightSql);
            String createPassengerSql = """
                    CREATE TABLE IF NOT EXISTS Passengers( 
                    id SERIAL PRIMARY KEY,
                    creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updatedTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    firstName VARCHAR(64) NOT NULL,
                    lastName VARCHAR(64) NOT NULL,
                    gender VARCHAR(12) NOT NULL,
                    nationality varchar(64) NOT NULL,
                    passportNumber varchar(20),
                    phoneNumber VARCHAR(15)
                    )
                    """;
            stmt.executeUpdate(createPassengerSql);
            String createBookingSql = """
                    CREATE TABLE IF NOT EXISTS Bookings(
                        id Serial PRIMARY KEY,
                        creationTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updatedTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        bookingPrice numeric NOT NULL,
                        seatNumber integer,
                        bookingDate timestamp NOT NULL,
                        flightID INT REFERENCES Flights(id)
                    )
                    """;
            stmt.executeUpdate(createBookingSql);
            String createBookingPassenger = """
                    CREATE TABLE IF NOT EXISTS BookingPassengers(
                        BookingID INT REFERENCES Bookings(id),
                        PassengerID INT REFERENCES Passengers(id)
                    )
                    """;
            stmt.executeUpdate(createBookingPassenger);
            System.out.println("Tables created successfully");
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.TABLE_CREATION_ERROR);
        }
    }
}


