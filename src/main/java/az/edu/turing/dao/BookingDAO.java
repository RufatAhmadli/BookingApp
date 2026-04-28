package az.edu.turing.dao;

import az.edu.turing.model.entity.Booking;

import java.util.List;

public interface BookingDAO extends DAO<Booking> {
    boolean deleteByPassengerId(Long passengerId);
    List<Booking> findByPassengerFullName(String passengerName);
}
