package az.edu.turing.exceptions;
import az.edu.turing.enums.EXCEPTIONS;

public class GeneralExceptions extends RuntimeException {
    public GeneralExceptions(EXCEPTIONS exception) {
        super(exception.toString());
    }
}
