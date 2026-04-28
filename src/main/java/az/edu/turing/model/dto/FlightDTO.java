package az.edu.turing.model.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class FlightDTO {
    private Long flightId;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer seatsNumber;
    private Integer availableSeats;
    private String flightType;
    private String flightCompany;

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getSeatsNumber() {
        return seatsNumber;
    }

    public void setSeatsNumber(Integer seatsNumber) {
        this.seatsNumber = seatsNumber;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getFlightType() {
        return flightType;
    }

    public void setFlightType(String flightType) {
        this.flightType = flightType;
    }

    public String getFlightCompany() {
        return flightCompany;
    }

    public void setFlightCompany(String flightCompany) {
        this.flightCompany = flightCompany;
    }

    @Override
    public String toString() {
        return "FlightDTO{" +
                "flightId=" + flightId +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", seatsNumber=" + seatsNumber +
                ", availableSeats=" + availableSeats +
                ", flightType='" + flightType + '\'' +
                ", flightCompany='" + flightCompany + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FlightDTO flightDTO)) return false;
        return Objects.equals(flightId, flightDTO.flightId) && Objects.equals(origin, flightDTO.origin) && Objects.equals(destination, flightDTO.destination) && Objects.equals(departureTime, flightDTO.departureTime) && Objects.equals(arrivalTime, flightDTO.arrivalTime) && Objects.equals(seatsNumber, flightDTO.seatsNumber) && Objects.equals(availableSeats, flightDTO.availableSeats) && Objects.equals(flightType, flightDTO.flightType) && Objects.equals(flightCompany, flightDTO.flightCompany);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightId, origin, destination, departureTime, arrivalTime, seatsNumber, availableSeats, flightType, flightCompany);
    }
}
