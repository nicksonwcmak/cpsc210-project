package model.exceptions;

public class DuplicatePersonException extends AlreadyInGroupException {

    public DuplicatePersonException() {
        super("There is already a person with this name in the group");
    }
}
