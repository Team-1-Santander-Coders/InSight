package getterson.insight.exceptions.user;

public class DuplicatedUserException extends Exception {
    public DuplicatedUserException(){
        super("Usuário já cadastrado");
    }
}
