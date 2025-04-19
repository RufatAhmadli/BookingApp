package az.edu.turing.service;

import az.edu.turing.enums.EXCEPTIONS;
import az.edu.turing.exceptions.GeneralExceptions;
import az.edu.turing.model.dto.PassengerDTO;
import az.edu.turing.model.mapper.PassengerMapper;
import az.edu.turing.repository.PassengerDAOImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class PassengerService implements Service<PassengerDTO> {
    PassengerMapper passengerMapper;
    PassengerDAOImpl repository;

    public PassengerService(PassengerMapper passengerMapper, PassengerDAOImpl repository) {
        this.passengerMapper = passengerMapper;
        this.repository = repository;
    }

    @Override
    public PassengerDTO create(PassengerDTO passengerDTO) {
        return Optional.ofNullable(passengerDTO)
                .map(passengerDTO1 -> passengerMapper.toEntity(passengerDTO1))
                .map(repository::save)
                .map(passengerMapper::toDTO)
                .orElseThrow(() -> new GeneralExceptions(EXCEPTIONS.PASSENGER_NOT_CREATED));
    }

    @Override
    public PassengerDTO update(Long id, PassengerDTO passengerDTO) {
        if (id == null || id <= 0) {
            throw new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        return repository.findById(id)
                .map(passenger -> passengerMapper.toEntity(passenger, passengerDTO))
                .map(repository::save)
                .map(passengerMapper::toDTO)
                .orElseThrow(() -> new GeneralExceptions(EXCEPTIONS.PASSENGER_NOT_UPDATED));
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null || id <= 0) {
            throw new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        boolean deleted = repository.deleteById(id);
        if (!deleted) {
            throw new GeneralExceptions(EXCEPTIONS.PASSENGER_NOT_FOUND);
        }
        return true;
    }

    @Override
    public List<PassengerDTO> list() {
        return repository.findAll()
                .stream()
                .map(passengerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PassengerDTO findById(Long id) {
        return repository.findById(id)
                .map(passengerMapper::toDTO)
                .orElseThrow(() -> new GeneralExceptions(EXCEPTIONS.PASSENGER_NOT_FOUND));
    }

    public boolean deleteByBookingId(Long bookingId) {
        if (bookingId == null || bookingId <= 0) {
            throw new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        boolean deleted = repository.deleteByBookingId(bookingId);
        if (!deleted) {
            throw new GeneralExceptions(EXCEPTIONS.PASSENGER_NOT_FOUND);
        }
        return true;
    }
}
