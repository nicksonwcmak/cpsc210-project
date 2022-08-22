package model.exceptions;

public class NotInGroupException extends Exception {

    public NotInGroupException() {
        super("The group does not contain a person with that name");
    }
}
