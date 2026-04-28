package az.edu.turing.repository;

import az.edu.turing.config.DatabaseConfig;
import az.edu.turing.dao.PassengersDAO;
import az.edu.turing.enums.EXCEPTIONS;
import az.edu.turing.exceptions.GeneralExceptions;
import az.edu.turing.model.entity.Passenger;
import az.edu.turing.model.mapper.PassengerMapper;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PassengerDAOImpl implements PassengersDAO {

    @Override
    public Passenger save(Passenger passenger) {
        if (passenger.getId() == null || passenger.getId() == 0) {
            return create(passenger);
        } else {
            return update(passenger);
        }
    }

    @Override
    public Passenger create(Passenger passenger) {
        String sql = """
                INSERT INTO passengers(creationTime,updatedTime,firstName,lastName,gender,nationality,passportNumber,phoneNumber)
                VALUES(?,?,?,?,?,?,?,?)
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            LocalDateTime now = LocalDateTime.now();
            stmt.setTimestamp(1, Timestamp.valueOf(now));
            stmt.setTimestamp(2, Timestamp.valueOf(now));
            stmt.setString(3, passenger.getFirstName());
            stmt.setString(4, passenger.getLastName());
            stmt.setString(5, passenger.getGender().toString());
            stmt.setString(6, passenger.getNationality());
            stmt.setString(7, passenger.getPassportNumber());
            stmt.setString(8, passenger.getPhoneNumber());
            stmt.executeUpdate();

            passenger.setCreatedAt(now);
            passenger.setUpdatedAt(now);
            return passenger;
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }

    }

    @Override
    public Passenger update(Passenger passenger) {
        String sql = """
                UPDATE PASSENGERS SET updatedTime=?,firstName=?,lastName=?,gender=?,nationality=?,passportNumber=?,phoneNumber=?
                WHERE id=?;
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(2, passenger.getFirstName());
            stmt.setString(3, passenger.getLastName());
            stmt.setString(4, passenger.getGender().toString());
            stmt.setString(5, passenger.getNationality());
            stmt.setString(6, passenger.getPassportNumber());
            stmt.setString(7, passenger.getPhoneNumber());
            stmt.setLong(8, passenger.getId());
            return passenger;
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
    }

    @Override
    public List<Passenger> findAll() {
        List<Passenger> passengers = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM passengers;")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                passengers.add(PassengerMapper.mapResultSetToPassenger(rs));
            }
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
        return passengers;
    }

    @Override
    public Optional<Passenger> findById(Long id) {
        if (id == null || id <= 0) {
            throw new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        String sql = """
                SELECT * FROM PASSENGERS where id=?;
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(PassengerMapper.mapResultSetToPassenger(rs));
            }

        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null || id <= 0) {
            throw new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        String sql = "DELETE FROM PASSENGERS where id=?;";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
    }


    @Override
    public boolean deleteByBookingId(Long bookingId) {
        if(bookingId == null || bookingId <= 0) {
            throw new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        String sql = """
                DELETE FROM passengers p
                USING BookingPassengers b 
                WHERE p.id=b.passengerId
                AND b.passengerId=?;
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, bookingId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
    }
}
