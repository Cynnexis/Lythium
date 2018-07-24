package fr.berger.jexec;

public class CommandException extends RuntimeException {
	
	public CommandException(String message) {
		super(message);
	}
	public CommandException() {
		super();
	}
}
