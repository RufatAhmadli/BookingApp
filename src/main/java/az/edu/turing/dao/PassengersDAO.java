package az.edu.turing.dao;

import az.edu.turing.model.entity.Passenger;

public interface PassengersDAO extends DAO<Passenger> {
    boolean deleteByBookingId(Long bookingId);
}
