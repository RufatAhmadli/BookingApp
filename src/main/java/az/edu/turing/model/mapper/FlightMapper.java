package az.edu.turing.model.mapper;

import az.edu.turing.model.dto.FlightDTO;
import az.edu.turing.model.entity.Flight;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class FlightMapper implements Mapper<Flight, FlightDTO> {
    public static Flight mapResultSetToFlight(ResultSet rs) throws SQLException {
        Flight flight = new Flight();
        flight.setId(rs.getLong("id"));
        flight.setCreatedAt(rs.getTimestamp("creationTime").toLocalDateTime());
        flight.setUpdatedAt(rs.getTimestamp("updatedTime").toLocalDateTime());
        flight.setOrigin(rs.getString("origin"));
        flight.setDestination(rs.getString("destination"));
        flight.setDepartureTime(rs.getTimestamp("departureTime").toLocalDateTime());
        flight.setArrivalTime(rs.getTimestamp("arrivalTime").toLocalDateTime());
        flight.setSeatsNumber(rs.getInt("seatsNumber"));
        flight.setAvailableSeats(rs.getInt("availableSeats"));
        flight.setFlightType(rs.getString("flightType"));
        flight.setFlightCompany(rs.getString("flightCompany"));
        return flight;
    }

    public FlightDTO toDTO(Flight flight) {
        if (flight == null) return null;

        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setFlightId(flight.getId());
        flightDTO.setOrigin(flight.getOrigin());
        flightDTO.setDestination(flight.getDestination());
        flightDTO.setSeatsNumber(flight.getSeatsNumber());
        flightDTO.setAvailableSeats(flight.getAvailableSeats());
        flightDTO.setArrivalTime(flight.getArrivalTime());
        flightDTO.setDepartureTime(flight.getDepartureTime());
        flightDTO.setFlightType(flight.getFlightType());
        flightDTO.setFlightCompany(flight.getFlightCompany());
        return flightDTO;
    }

    public Flight toEntity(FlightDTO dto) {
        if (dto == null) return null;

        Flight flight = new Flight();
        return assignFields(flight, dto);
    }

    @Override
    public Flight toEntity(Flight flight, FlightDTO flightDTO) {
        if (flightDTO == null) return null;
        return assignFields(flight, flightDTO);
    }

    private Flight assignFields(Flight flight, FlightDTO flightDTO) {
        flight.setId(flightDTO.getFlightId());
        flight.setOrigin(flightDTO.getOrigin());
        flight.setDestination(flightDTO.getDestination());
        flight.setSeatsNumber(flightDTO.getSeatsNumber());
        flight.setAvailableSeats(flightDTO.getAvailableSeats());
        flight.setDepartureTime(flightDTO.getDepartureTime());
        flight.setArrivalTime(flightDTO.getArrivalTime());
        flight.setFlightType(flightDTO.getFlightType());
        flight.setFlightCompany(flightDTO.getFlightCompany());
        return flight;
    }
}
