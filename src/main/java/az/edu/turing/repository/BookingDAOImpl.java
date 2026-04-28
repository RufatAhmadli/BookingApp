package az.edu.turing.repository;

import az.edu.turing.config.DatabaseConfig;
import az.edu.turing.dao.BookingDAO;
import az.edu.turing.enums.EXCEPTIONS;
import az.edu.turing.exceptions.GeneralExceptions;
import az.edu.turing.model.entity.Booking;
import az.edu.turing.model.mapper.BookingMapper;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingDAOImpl implements BookingDAO {

    @Override
    public Booking save(Booking booking) {
        if (booking.getId() == null || booking.getId() == 0) {
            return create(booking);
        } else {
            return update(booking);
        }
    }

    @Override
    public Booking create(Booking booking) {
        String sql = """
                INSERT INTO bookings(creationTime,updatedTime,bookingPrice,seatNumber,bookingDate,flightId) 
                VALUES (?,?,?,?,?,?);
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            LocalDateTime now = LocalDateTime.now();
            stmt.setTimestamp(1, Timestamp.valueOf(now));
            stmt.setTimestamp(2, Timestamp.valueOf(now));
            stmt.setDouble(3, booking.getBookingPrice());
            stmt.setLong(4, booking.getSeatNumber());
            stmt.setTimestamp(5, Timestamp.valueOf(now));
            stmt.setLong(6, booking.getFlightId());
            stmt.executeUpdate();

            booking.setCreatedAt(now);
            booking.setUpdatedAt(now);
            return booking;
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
    }

    @Override
    public Booking update(Booking booking) {
        String sql = """
                UPDATE bookings
                SET updatedTime=?,bookingPrice=?,seatNumber=?,bookingDate=?,flightid=? WHERE id=?;
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            LocalDateTime now = LocalDateTime.now();
            stmt.setTimestamp(1, Timestamp.valueOf(now));
            stmt.setDouble(2, booking.getBookingPrice());
            stmt.setLong(3, booking.getSeatNumber());
            stmt.setTimestamp(4, Timestamp.valueOf(now));
            stmt.setLong(5, booking.getFlightId());
            stmt.setLong(6, booking.getId());
            stmt.executeUpdate();

            booking.setUpdatedAt(now);
            return booking;
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM bookings")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(BookingMapper.mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
        return bookings;
    }

    @Override
    public Optional<Booking> findById(Long id) {
        if (id == null || id <= 0) {
            throw new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        String sql = """
                SELECT * FROM bookings WHERE id=?;
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(BookingMapper.mapResultSetToBooking(rs));
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
        String sql = """
                DELETE FROM bookings WHERE id=?;
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
    }

    @Override
    public boolean deleteByPassengerId(Long passengerId) {
        if (passengerId == null || passengerId <= 0) {
            throw new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        String sql = """
                DELETE FROM bookings b
                USING bookingPassengers bp
                WHERE b.id = bp.bookingid
                AND bp.passengerid = ?;
                """;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, passengerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
    }

    @Override
    public List<Booking> findByPassengerFullName(String passengerName) {
        String[] nameParts = passengerName.trim().split(" ");
        if (nameParts.length < 2) { //Just for people who have single name and surname
            throw new GeneralExceptions(EXCEPTIONS.INVALID_FULLNAME);
        }
        String firstName = nameParts[0];
        String lastName = nameParts[1];

        String sql= """
                SELECT *  FROM bookings b
                JOIN bookingPassengers bp ON bp.bookingid = b.id
                JOIN passengers p ON p.id = bp.passengerid
                WHERE Lower(p.firstname) = Lower(?) AND Lower(p.lastname)=Lower(?);
                """;
        List<Booking> bookings = new ArrayList<>();
        try(Connection conn=DatabaseConfig.getConnection();
        PreparedStatement stmt= conn.prepareStatement(sql)){
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(BookingMapper.mapResultSetToBooking(rs));
            }
        }catch(SQLException e){
            throw new GeneralExceptions(EXCEPTIONS.DATABASE_CONNECTION_ERROR);
        }
        if(bookings.isEmpty()) throw new GeneralExceptions(EXCEPTIONS.INVALID_FULLNAME);
        return bookings;
    }
}
