package az.edu.turing.dao;

import az.edu.turing.model.entity.Flight;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightDAO extends DAO<Flight> {
    List<Flight> findByDestination(String destination);
    List<Flight> findBySeatsNumber(Integer seatNumber);
    List<Flight> findByFlightDate(LocalDateTime flightDate);
}
