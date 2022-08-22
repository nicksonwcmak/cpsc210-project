package model.exceptions;

public class DuplicateConnectionException extends AlreadyInGroupException {

    public DuplicateConnectionException() {
        super("There is already a connection between these people");
    }
}
