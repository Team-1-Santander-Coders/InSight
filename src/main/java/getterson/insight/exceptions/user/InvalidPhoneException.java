package getterson.insight.exceptions.user;

public class InvalidPhoneException extends Exception {
    public InvalidPhoneException() {
        super("O número de telefone informado é inválido.");
    }
}
