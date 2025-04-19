package az.edu.turing.model.mapper;

import az.edu.turing.model.dto.BookingDTO;
import az.edu.turing.model.entity.Booking;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class BookingMapper implements Mapper<Booking,BookingDTO>{
    public static Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getLong("id"));
        booking.setCreatedAt(rs.getTimestamp("creationTime").toLocalDateTime());
        booking.setUpdatedAt(rs.getTimestamp("updatedTime").toLocalDateTime());
        booking.setBookingPrice(rs.getDouble("bookingPrice"));
        booking.setSeatNumber(rs.getLong("seatNumber"));
        booking.setBookingDate(rs.getTimestamp("bookingDate").toLocalDateTime());
        booking.setFlightId(rs.getLong("flightId"));
        return booking;
    }


    @Override
    public BookingDTO toDTO(Booking booking) {
        if(booking == null) return null;

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setBookingId(booking.getId());
        bookingDTO.setBookingDate(booking.getBookingDate());
        bookingDTO.setSeatNumber(booking.getSeatNumber());
        bookingDTO.setFlightId(booking.getFlightId());
        return bookingDTO;
    }

    @Override
    public Booking toEntity(BookingDTO bookingDTO) {
        if(bookingDTO == null) return null;

        Booking booking = new Booking();
        assignFields(booking, bookingDTO);
        return booking;
    }

    @Override
    public Booking toEntity(Booking booking, BookingDTO bookingDTO) {
        if(bookingDTO == null) return null;
        assignFields(booking, bookingDTO);
        return booking;
    }

    private void assignFields(Booking booking, BookingDTO bookingDTO) {
        booking.setId(bookingDTO.getBookingId());
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setSeatNumber(bookingDTO.getSeatNumber());
        booking.setFlightId(bookingDTO.getFlightId());
    }
}
