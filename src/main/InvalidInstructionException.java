package main;

/**
 * Created by JHGWhite on 26/04/2016.
 */

/*
 * Exception to be thrown if user instruction is
 * of unrecognised format.
 */
public class InvalidInstructionException extends Exception {

    public InvalidInstructionException(String message) {
        super(message);
    }

}
