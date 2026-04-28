package az.edu.turing.repository;

import az.edu.turing.config.DatabaseConfig;
import az.edu.turing.dao.FlightDAO;
import az.edu.turing.enums.EXCEPTIONS;
import az.edu.turing.exceptions.GeneralExceptions;
import az.edu.turing.model.entity.Flight;
import az.edu.turing.model.mapper.FlightMapper;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightDAOImpl implements FlightDAO {
    @Override
    public Flight save(Flight flight) {
        if (flight.getId() == null || flight.getId() == 0) {
            return create(flight);
        } else {
            return update(flight);
        }
    }

    @Override
    public Flight create(Flight flight) {
        String sql = """
                INSERT INTO flights(creationTime,updatedTime,origin,destination,departureTime,arrivalTime,seatsNumber,availableSeats,flightType,flightCompany)  
                VALUES(?,?,?,?,?,?,?,?,?,?)
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            LocalDateTime now = LocalDateTime.now();
            stmt.setTimestamp(1, Timestamp.valueOf(now));
            stmt.setTimestamp(2, Timestamp.valueOf(now));
            stmt.setString(3, flight.getOrigin());
            stmt.setString(4, flight.getDestination());
            stmt.setTimestamp(5, Timestamp.valueOf(flight.getDepartureTime()));
            stmt.setTimestamp(6, Timestamp.valueOf(flight.getArrivalTime()));
            stmt.setInt(7, flight.getSeatsNumber());
            stmt.setInt(8, flight.getAvailableSeats());
            stmt.setString(9, flight.getFlightType());
            stmt.setString(10, flight.getFlightCompany());
            stmt.executeUpdate();

            flight.setCreatedAt(now);
            flight.setUpdatedAt(now);
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
        return flight;

    }

    @Override
    public Flight update(Flight flight) {
        String sql = """
                Update flights
                SET updatedTime=?,origin=?,destination=?,departureTime=?,arrivalTime=?,seatsnumber=?,availableSeats=?,
                flightType=?,flightCompany=? where id=?
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            LocalDateTime now = LocalDateTime.now();
            stmt.setTimestamp(1, Timestamp.valueOf(now));
            stmt.setString(2, flight.getOrigin());
            stmt.setString(3, flight.getDestination());
            stmt.setTimestamp(4, Timestamp.valueOf(flight.getDepartureTime()));
            stmt.setTimestamp(5, Timestamp.valueOf(flight.getArrivalTime()));
            stmt.setInt(6, flight.getSeatsNumber());
            stmt.setInt(7, flight.getAvailableSeats());
            stmt.setString(8, flight.getFlightType());
            stmt.setString(9, flight.getFlightCompany());
            stmt.setLong(10, flight.getId());
            stmt.executeUpdate();

            flight.setUpdatedAt(now);
            return flight;
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
    }

    @Override
    public List<Flight> findAll() {
        List<Flight> flights = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM flights")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                flights.add(FlightMapper.mapResultSetToFlight(rs));
            }
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
        return flights;
    }

    @Override
    public Optional<Flight> findById(Long id) {
        if (id == null || id <= 0) {
            throw new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        String sql = """
                SELECT * FROM flights  WHERE id=?
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(FlightMapper.mapResultSetToFlight(rs));
            }else{
                throw new GeneralExceptions(EXCEPTIONS.FLIGHT_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null || id <= 0) {
            throw new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        String sql = "DELETE FROM flights WHERE id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
    }

    @Override
    public List<Flight> findByDestination(String destination) {
        List<Flight> flights = new ArrayList<>();
        String sql = """
                SELECT * FROM flights  WHERE destination=?
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, destination);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                flights.add(FlightMapper.mapResultSetToFlight(rs));
            }
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
        return flights;
    }

    @Override
    public List<Flight> findBySeatsNumber(Integer seatNumber) {
        List<Flight> flights = new ArrayList<>();
        String sql = """
                SELECT * FROM flights  WHERE flights.availableseats>=?
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1, seatNumber);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                flights.add(FlightMapper.mapResultSetToFlight(rs));
            }
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
        return flights;
    }

    @Override
    public List<Flight> findByFlightDate(LocalDateTime flightDate) {
        List<Flight> flights = new ArrayList<>();
        String sql = """
                SELECT * FROM flights  WHERE departuretime>=?
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setTimestamp(1, Timestamp.valueOf(flightDate));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                flights.add(FlightMapper.mapResultSetToFlight(rs));
            }
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
        return flights;
    }
}
