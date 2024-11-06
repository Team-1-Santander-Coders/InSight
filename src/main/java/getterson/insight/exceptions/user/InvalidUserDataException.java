package getterson.insight.exceptions.user;

public class InvalidUserDataException extends Exception{
    public InvalidUserDataException(){
        super("Nenhum usuário encontrado com os dados passados");
    }
}
