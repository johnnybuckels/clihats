package io.github.johannesbuchholz.clihats.core.exceptions.execution;

import io.github.johannesbuchholz.clihats.core.execution.Command;
import io.github.johannesbuchholz.clihats.core.execution.ParsingResult;
import io.github.johannesbuchholz.clihats.util.TextUtils;

import java.util.LinkedList;
import java.util.List;

public class ParsingException extends CommandExecutionException {

    /**
     * Constructs a message from the presumably invalid parsingState.
     */
    public ParsingException(Command failingCommand, ParsingResult parsingResult) {
        super(failingCommand, generateErrorMessage(parsingResult));
    }

    private static String generateErrorMessage(ParsingResult parsingResult) {
        List<String> messageLines = new LinkedList<>();
        if (!parsingResult.getErrors().isEmpty()) {
            messageLines.add("> Parsing errors:");
            parsingResult.getErrors()
                    .forEach(error -> messageLines.add(TextUtils.indentEveryLine(error.getMessage())));
        }
        if (!parsingResult.getMissing().isEmpty()) {
            messageLines.add("> Missing required arguments:");
            parsingResult.getMissing()
                    .forEach(missingParser -> messageLines.add(TextUtils.indentEveryLine(missingParser.getPrimaryName())));
        }
        if (!parsingResult.getUnknown().isEmpty()) {
            messageLines.add("> Unknown arguments:");
            parsingResult.getUnknown()
                    .forEach(unknownArg -> messageLines.add(TextUtils.indentEveryLine(unknownArg)));
        }
        return String.format("Invalid input arguments:\n%s", String.join("\n", messageLines));
    }

}
