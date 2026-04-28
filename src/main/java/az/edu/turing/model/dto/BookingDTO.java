package az.edu.turing.model.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class BookingDTO {
    private Long bookingId;
    private Long flightId;
    private Long seatNumber;
    private LocalDateTime bookingDate;


    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public Long getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Long seatNumber) {
        this.seatNumber = seatNumber;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    @Override
    public String toString() {
        return "BookingDTO{" +
                "bookingId=" + bookingId +
                ", flightId=" + flightId +
                ", seatNumber=" + seatNumber +
                ", bookingDate=" + bookingDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BookingDTO that)) return false;
        return Objects.equals(bookingId, that.bookingId) && Objects.equals(flightId, that.flightId) && Objects.equals(seatNumber, that.seatNumber) && Objects.equals(bookingDate, that.bookingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, flightId, seatNumber, bookingDate);
    }
}
