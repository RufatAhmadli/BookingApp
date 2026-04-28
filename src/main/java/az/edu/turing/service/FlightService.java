package az.edu.turing.service;

import az.edu.turing.enums.EXCEPTIONS;
import az.edu.turing.exceptions.GeneralExceptions;
import az.edu.turing.model.dto.FlightDTO;
import az.edu.turing.model.entity.Flight;
import az.edu.turing.model.mapper.FlightMapper;
import az.edu.turing.repository.FlightDAOImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FlightService implements Service<FlightDTO> {
    FlightMapper flightMapper;
    FlightDAOImpl repository;

    public FlightService(FlightMapper flightMapper, FlightDAOImpl repository) {
        this.flightMapper = flightMapper;
        this.repository = repository;
    }

    @Override
    public FlightDTO create(FlightDTO flightDTO) {
        return Optional.ofNullable(flightDTO)
                .map(flightMapper::toEntity)
                .map(repository::save)
                .map(flightMapper::toDTO)
                .orElseThrow(() -> new GeneralExceptions(EXCEPTIONS.FLIGHT_NOT_CREATED));
    }

    @Override
    public FlightDTO update(Long id, FlightDTO flightDTO) {
        if (id == null || id <= 0) {
            throw new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        return repository.findById(id)
                .map(flight -> flightMapper.toEntity(flight,flightDTO))
                .map(repository::save)
                .map(flightMapper::toDTO)
                .orElseThrow(() -> new GeneralExceptions(EXCEPTIONS.FLIGHT_NOT_UPDATED));
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null || id <= 0) {
            throw new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        boolean deleted = repository.deleteById(id);
        if (!deleted) {
            throw  new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        return true;
    }

    @Override
    public List<FlightDTO> list() {
        return repository.findAll()
                .stream()
                .map(flightMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FlightDTO findById(Long id) {
        if (id == null || id <= 0) {
            throw new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        return repository.findById(id)
                .map(flightMapper::toDTO)
                .orElseThrow(() -> new GeneralExceptions(EXCEPTIONS.FLIGHT_NOT_FOUND));
    }

    public List<FlightDTO> findByDestination(String destination){
        return repository.findByDestination(destination)
                .stream()
                .map(flightMapper::toDTO)
                .collect(Collectors.toList());
    }
    public List<FlightDTO> findBySeatsNumber(Integer seatNumber){
        return repository.findBySeatsNumber(seatNumber)
                .stream()
                .map(flightMapper::toDTO)
                .collect(Collectors.toList());
    }
    public List<FlightDTO> findByFlightDate(LocalDateTime flightDate){
        return repository.findByFlightDate(flightDate)
                .stream()
                .map(flightMapper::toDTO)
                .collect(Collectors.toList());
    }
}
