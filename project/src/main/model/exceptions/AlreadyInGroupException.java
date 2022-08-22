package model.exceptions;

// exception when trying to add an element that is already in the group
public abstract class AlreadyInGroupException extends RuntimeException {
    AlreadyInGroupException(String message) {
        super(message);
    }
}
