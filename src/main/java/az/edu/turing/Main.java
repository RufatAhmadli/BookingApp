package az.edu.turing;

import az.edu.turing.enums.EXCEPTIONS;
import az.edu.turing.enums.GENDERS;
import az.edu.turing.exceptions.GeneralExceptions;
import az.edu.turing.model.dto.BookingDTO;
import az.edu.turing.model.dto.FlightDTO;
import az.edu.turing.model.dto.PassengerDTO;
import az.edu.turing.model.mapper.BookingMapper;
import az.edu.turing.model.mapper.FlightMapper;
import az.edu.turing.model.mapper.PassengerMapper;
import az.edu.turing.repository.BookingDAOImpl;
import az.edu.turing.repository.FlightDAOImpl;
import az.edu.turing.repository.PassengerDAOImpl;
import az.edu.turing.service.BookingService;
import az.edu.turing.service.FlightService;
import az.edu.turing.service.PassengerService;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    static FlightService service = new FlightService(new FlightMapper(), new FlightDAOImpl());
    static BookingService bookingService = new BookingService(new BookingMapper(), new BookingDAOImpl());
    static PassengerService passengerService = new PassengerService(new PassengerMapper(), new PassengerDAOImpl());
    static Scanner scanner = new Scanner(System.in);
    static String bookingMenu = """
            0: Return to main menu
            1: Book a flight
            """;

    public static void main(String[] args) {
        String mainMenu = """
                1.Online-board
                2.Show the flight information
                3.Search and book the flight
                4.Cancel the booking
                5.My flights
                6.Exit
                """;
        while (true) {
            System.out.println(mainMenu);
            System.out.println("Please enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> onlineBoard();
                case 2 -> showFlight();
                case 3 -> bookMenu();
                case 4 -> cancelBooking();
                case 5 -> showMyFlights();
                case 6 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static void showMyFlights() {
        while (true) {
            System.out.println("Please enter your full name: ");
            String fullName = scanner.nextLine();
            List<BookingDTO> byPassengerFullName = bookingService.findByPassengerFullName(fullName);
            byPassengerFullName.forEach(System.out::println);
            System.out.println("Enter 1 to return to main menu: ");
            int choice = scanner.nextInt();
            if (choice == 1) break;
        }

    }

    private static void cancelBooking() {
        while (true) {
            System.out.println("Please enter the booking ID you want to cancel: ");
            Long bookingId = scanner.nextLong();
            scanner.nextLine();

            BookingDTO byId = bookingService.findById(bookingId);
            System.out.println(byId);
            System.out.println("Enter 1 to cancel: ");
            int choice = scanner.nextInt();
            if (choice != 1) {
                System.out.println("Returning to main menu.....");
                break;
            }

            bookingService.deleteById(bookingId);
            FlightDTO flightDTO = service.findById(byId.getFlightId());
            flightDTO.setAvailableSeats(flightDTO.getAvailableSeats() + byId.getSeatNumber().intValue());
            passengerService.deleteByBookingId(bookingId);
            System.out.println("Booking has been cancelled");
            break;
        }

    }

    private static void bookMenu() {
        while (true) {
            scanner.nextLine();
            System.out.println("Please enter the destination you want to book: ");
            String destination = scanner.nextLine();

            System.out.println("Please enter the date you want to book (yyyy-MM-dd HH:mm): ");
            String dateInput = scanner.nextLine();
            LocalDateTime date;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                date = LocalDateTime.parse(dateInput, formatter);
            } catch (Exception e) {
                throw new GeneralExceptions(EXCEPTIONS.INVALID_DATE_FORMAT);
            }

            System.out.println("Please enter the needed number of seats you want to book: ");
            int numberOfSeats = scanner.nextInt();
            scanner.nextLine();

            List<FlightDTO> byDestination = service.findByDestination(destination);
            List<FlightDTO> bySeatsNumber = service.findBySeatsNumber(numberOfSeats);
            List<FlightDTO> byFlightDate = service.findByFlightDate(date);

            List<FlightDTO> filteredFlights = byFlightDate.stream()
                    .filter(bySeatsNumber::contains)
                    .filter(byDestination::contains)
                    .toList();
            if (filteredFlights.isEmpty()) {
                System.out.println("There are no flights for the given conditions.");
                break;
            } else {
                filteredFlights.forEach(System.out::println);
            }

            System.out.println(bookingMenu);
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 0 -> {
                    return;
                }
                case 1 -> bookFlight(filteredFlights, numberOfSeats);
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void bookFlight(List<FlightDTO> filteredFlights, Integer numberOfSeats) {
        System.out.println("Please enter the id you want to book: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        FlightDTO selectedFlightDTO = filteredFlights.stream()
                .filter(flightDTO -> flightDTO.getFlightId().equals(id))
                .findFirst()
                .orElseThrow(() -> new GeneralExceptions(EXCEPTIONS.FLIGHT_NOT_FOUND));

        selectedFlightDTO.setAvailableSeats(selectedFlightDTO.getSeatsNumber() - numberOfSeats);
        service.update(selectedFlightDTO.getFlightId(), selectedFlightDTO);
        for (int i = 0; i < numberOfSeats; i++) {
            System.out.println("Passenger " + (i + 1) + " info:");
            System.out.print("Full name: ");
            String fullName = scanner.nextLine();
            System.out.print("Gender (MALE/FEMALE/OTHER): ");
            String gender = scanner.nextLine();
            System.out.print("Nationality: ");
            String nationality = scanner.nextLine();
            System.out.print("Passport Number: ");
            String passport = scanner.nextLine();
            System.out.print("Phone Number: ");
            String phone = scanner.nextLine();

            PassengerDTO passengerDTO = new PassengerDTO();
            passengerDTO.setFullName(fullName);
            passengerDTO.setGender(GENDERS.valueOf(gender.toUpperCase()));
            passengerDTO.setNationality(nationality);
            passengerDTO.setPassportNumber(passport);
            passengerDTO.setPhoneNumber(phone);

            passengerService.create(passengerDTO);
        }
        selectedFlightDTO.setAvailableSeats(selectedFlightDTO.getAvailableSeats() - numberOfSeats);
        service.update(selectedFlightDTO.getFlightId(), selectedFlightDTO);


        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setFlightId(selectedFlightDTO.getFlightId());
        bookingDTO.setSeatNumber((long) numberOfSeats);
        bookingDTO.setBookingDate(LocalDateTime.now());

        bookingService.create(bookingDTO);

        System.out.println("Booking successful! " + numberOfSeats + " seat(s) booked.");
    }

    private static void showFlight() {
        System.out.println("Please enter the flight number: ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        System.out.println(service.findById(id));
    }

    private static void onlineBoard() {
        System.out.println("Available flights: ");
        service.list().forEach(System.out::println);
    }
}