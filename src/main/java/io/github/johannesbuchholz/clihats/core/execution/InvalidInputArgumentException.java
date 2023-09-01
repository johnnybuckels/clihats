package io.github.johannesbuchholz.clihats.core.execution;

import io.github.johannesbuchholz.clihats.core.execution.parser.ArgumentParsingException;

public class InvalidInputArgumentException extends CommandExecutionException {

    /**
     * Constructs a message from the presumably invalid parsingState.
     */
    public InvalidInputArgumentException(Command failingCommand, ArgumentParsingException cause) {
        super(failingCommand, "Invalid input arguments: " + cause.getMessage(), cause);
    }

}
