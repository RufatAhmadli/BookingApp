package az.edu.turing.model.mapper;

import az.edu.turing.enums.GENDERS;
import az.edu.turing.model.dto.PassengerDTO;
import az.edu.turing.model.entity.Passenger;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class PassengerMapper implements Mapper<Passenger, PassengerDTO> {
    public static Passenger mapResultSetToPassenger(ResultSet rs) throws SQLException {
        Passenger passenger = new Passenger();
        passenger.setId(rs.getLong("id"));
        passenger.setCreatedAt(rs.getTimestamp("creationTime").toLocalDateTime());
        passenger.setUpdatedAt(rs.getTimestamp("updatedTime").toLocalDateTime());
        passenger.setFirstName(rs.getString("firstName"));
        passenger.setLastName(rs.getString("lastName"));
        passenger.setGender(GENDERS.valueOf(rs.getString("gender")));
        passenger.setNationality(rs.getString("nationality"));
        passenger.setPassportNumber(rs.getString("passportNumber"));
        passenger.setPhoneNumber(rs.getString("phoneNumber"));
        return passenger;
    }

    @Override
    public PassengerDTO toDTO(Passenger passenger) {
        if (passenger == null) return null;

        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setPassengerId(passenger.getId());
        passengerDTO.setFullName(passenger.getFirstName() + " " + passenger.getLastName());
        passengerDTO.setGender(passenger.getGender());
        passengerDTO.setNationality(passenger.getNationality());
        passengerDTO.setPassportNumber(passenger.getPassportNumber());
        passengerDTO.setPhoneNumber(passenger.getPhoneNumber());
        return passengerDTO;
    }

    @Override
    public Passenger toEntity(PassengerDTO passengerDTO) {
        if (passengerDTO == null) return null;

        Passenger passenger = new Passenger();
        return setPassenger(passenger, passengerDTO);
    }

    @Override
    public Passenger toEntity(Passenger passenger, PassengerDTO passengerDTO) {
        if (passengerDTO == null) return null;
        return setPassenger(passenger, passengerDTO);
    }

    private Passenger setPassenger(Passenger passenger, PassengerDTO passengerDTO) {
        passenger.setId(passengerDTO.getPassengerId());
        splitFullName(passenger, passengerDTO);
        passenger.setGender(passengerDTO.getGender());
        passenger.setNationality(passengerDTO.getNationality());
        passenger.setPassportNumber(passengerDTO.getPassportNumber());
        passenger.setPhoneNumber(passengerDTO.getPhoneNumber());
        return passenger;
    }

    private void splitFullName(Passenger passenger, PassengerDTO passengerDTO) {
        String[] nameSurname = passengerDTO.getFullName().split(" ");
        passenger.setFirstName(nameSurname[0]);
        passenger.setLastName(nameSurname[1]);
    }

}
