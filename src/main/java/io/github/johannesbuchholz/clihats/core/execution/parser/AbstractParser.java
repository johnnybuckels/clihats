package io.github.johannesbuchholz.clihats.core.execution.parser;

import io.github.johannesbuchholz.clihats.core.execution.InputArgument;

import java.util.Objects;
import java.util.Optional;

/**
 * Extending classes are able to parse a value from a list of options.
 */
public abstract class AbstractParser<T> {

    /**
     * @return The displayable name of this parser.
     */
    public abstract String getDisplayName();

    public abstract ParserHelpContent getHelpContent();

    /**
     * Parses the argument from the specified index.
     * <p>
     *     Applies side effects on the argument array when using input arguments by replacing them with null
     *     or an updated InputArgument object.
     * </p>
     * @param inputArgs All available input arguments. Has length ov at least one.
     * @param index The index to be parsed. Does not point to a null element.
     * @return The parsed non-null value if this parser is applicable to the specified index and empty otherwise.
     * @throws MissingArgumentException If an unexpected exception occurred during parsing. Callers may discard this parser for future parsing attempts.
     * @apiNote The parser is expected to remove potentially used arguments from the specified array, even when throwing.
     */
    public abstract ArgumentParsingResult<T> parse(InputArgument[] inputArgs, int index) throws ArgumentParsingException;

    /**
     * @return The default value of this parser if any.
     * @throws MissingArgumentException If an unexpected exception occurred during parsing. Callers may discard this parser for future parsing attempts.
     */
    public abstract ArgumentParsingResult<T> defaultValue() throws ArgumentParsingException;

    /**
     * @param other Another parser.
     * @return A message describing the conflict with other or empty if not conflicting.
     */
    public abstract Optional<String> getConflictMessage(AbstractParser<?> other);

    /**
     * @return the desired priority to be called with. Lower values indicate earlier parsing.
     */
    // TODO: Consider removing this field and let Command have one field for option parsers and one for operand parsers.
    public abstract int getParsingPriority();

    T mapWithThrows(ValueMapper<T> mapper, String stringValue) throws ValueMappingException {
        if (stringValue == null)
            return null;
        try {
            return mapper.map(stringValue);
        } catch (Exception e) {
            throw new ValueMappingException(this, e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractParser<?> that = (AbstractParser<?>) o;
        return this.toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.toString());
    }

}