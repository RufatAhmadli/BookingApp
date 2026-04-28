package az.edu.turing.enums;

public enum EXCEPTIONS {

    DATABASE_CONNECTION_ERROR("Can't connect to database"),
    DATABASE_PROPERTIES_ERROR("Database properties not found"),
    DATABASE_CREATION_ERROR("Database cannot be created"),
    TABLE_CREATION_ERROR("Table cannot be created"),
    PASSENGER_NOT_CREATED("Passenger not created"),
    PASSENGER_NOT_FOUND("Passenger not found"),
    PASSENGER_NOT_UPDATED("Passenger not updated"),
    INVALID_ID("Invalid id"),
    FLIGHT_NOT_CREATED("Flight not created"),
    FLIGHT_NOT_FOUND("Flight not found"),
    FLIGHT_NOT_UPDATED("Flight not updated"),
    BOOKING_NOT_CREATED("Booking not created"),
    BOOKING_NOT_FOUND("Booking not found"),
    BOOKING_NOT_UPDATED("Booking not updated"),
    INVALID_CHOICE("Not a valid choice"),
    INVALID_FULLNAME("Not any passenger with this fullname"),
    INVALID_DATE_FORMAT("Invalid date format.Enter date: YYYY-MM-DD"),;

    private final String message;

    public String getMessage() {
        return message;
    }

    EXCEPTIONS(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return  message + '\n';
    }
}
