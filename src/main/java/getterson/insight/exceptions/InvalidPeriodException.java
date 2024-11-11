package getterson.insight.exceptions;

public class InvalidPeriodException extends Exception {
    public InvalidPeriodException() {
        super("O período informado deve ser DAY, WEEK ou MONTH.");
    }
}
