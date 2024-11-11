package getterson.insight.exceptions.user;

public class InvalidDocumentException extends Exception {
    public InvalidDocumentException() {
        super("O documento fornecido é inválido.");
    }
}
