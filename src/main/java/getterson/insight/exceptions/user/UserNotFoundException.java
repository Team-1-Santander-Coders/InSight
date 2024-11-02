package getterson.insight.exceptions.user;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(){
        super("Usuário não encontrado");
    }
}
