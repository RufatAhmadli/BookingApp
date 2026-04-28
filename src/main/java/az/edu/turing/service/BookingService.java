package az.edu.turing.service;

import az.edu.turing.enums.EXCEPTIONS;
import az.edu.turing.exceptions.GeneralExceptions;
import az.edu.turing.model.dto.BookingDTO;
import az.edu.turing.model.mapper.BookingMapper;
import az.edu.turing.repository.BookingDAOImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookingService implements Service<BookingDTO> {
    BookingMapper bookingMapper;
    BookingDAOImpl repository;

    public BookingService(BookingMapper bookingMapper, BookingDAOImpl repository) {
        this.bookingMapper = bookingMapper;
        this.repository = repository;
    }

    @Override
    public BookingDTO create(BookingDTO bookingDTO) {
        return Optional.ofNullable(bookingDTO)
                .map(bookingMapper::toEntity)
                .map(repository::save)
                .map(bookingMapper::toDTO)
                .orElseThrow(() -> new GeneralExceptions(EXCEPTIONS.BOOKING_NOT_CREATED));
    }

    @Override
    public BookingDTO update(Long id, BookingDTO bookingDTO) {
        if (id == null || id <= 0) {
            throw new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        return repository.findById(id)
                .map(booking -> bookingMapper.toEntity(booking, bookingDTO))
                .map(repository::save)
                .map(bookingMapper::toDTO)
                .orElseThrow(() -> new GeneralExceptions(EXCEPTIONS.BOOKING_NOT_UPDATED));
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null || id <= 0) {
            throw new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        boolean deleted = repository.deleteById(id);
        if (!deleted) {
            throw new GeneralExceptions(EXCEPTIONS.BOOKING_NOT_FOUND);
        }
        return true;
    }

    @Override
    public List<BookingDTO> list() {
        return repository.findAll()
                .stream()
                .map(bookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDTO findById(Long id) {
        if (id == null || id <= 0) {
            throw new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        return repository.findById(id)
                .map(bookingMapper::toDTO)
                .orElseThrow(() -> new GeneralExceptions(EXCEPTIONS.BOOKING_NOT_FOUND));
    }

    public boolean deleteByPassengerId(Long id) {
        if (id == null || id <= 0) {
            throw new GeneralExceptions(EXCEPTIONS.INVALID_ID);
        }
        boolean deleted = repository.deleteByPassengerId(id);
        if (!deleted) {
            throw new GeneralExceptions(EXCEPTIONS.BOOKING_NOT_FOUND);
        }
        return true;
    }

    public List<BookingDTO> findByPassengerFullName(String passengerFullName) {
        return repository.findByPassengerFullName(passengerFullName)
                .stream()
                .map(bookingMapper::toDTO)
                .collect(Collectors.toList());
    }
}
