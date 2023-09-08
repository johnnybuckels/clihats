package io.github.johannesbuchholz.clihats.core.execution.parser;

import io.github.johannesbuchholz.clihats.core.execution.InputArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * These parsers are always parsed during the last parsing round and parse the remaining argument directly
 * from their respective position.
 */
public class OperandParser<T> extends AbstractOperandParser<T> {

    private final int position;
    private final ValueMapper<T> valueMapper;
    private final String description;
    private final boolean required;
    private final Supplier<String> defaultSupplier;

    protected static OperandParser<String> at(int position) {
        if (position < 0) {
            throw new IllegalArgumentException("position needs to be non-negative but was " + position);
        }
        return new OperandParser<>(position, () -> null, false, stringValue -> stringValue, "");
    }

    private OperandParser(int position, Supplier<String> defaultSupplier, boolean required, ValueMapper<T> valueMapper, String description) {
        this.position = position;
        this.valueMapper = valueMapper;
        this.description =description;
        this.required = required;
        this.defaultSupplier = defaultSupplier;
    }

    /**
     * Returns a new PositionalArgument with this objects position and the given mapper.
     */
    public <X> OperandParser<X> withMapper(ValueMapper<X> mapper) {
        return new OperandParser<>(position, defaultSupplier, required, Objects.requireNonNull(mapper), description);
    }

    public OperandParser<T> withDescription(String description) {
        return new OperandParser<>(position, defaultSupplier, required, valueMapper,  Objects.requireNonNullElse(description, "").trim());
    }

    public OperandParser<T> withRequired(boolean required) {
        return new OperandParser<>(position, defaultSupplier, required, valueMapper, description);
    }

    public OperandParser<T> withDefault(String defaultValue) {
        return new OperandParser<>(position, () -> defaultValue, required, valueMapper, description);
    }

    public OperandParser<T> withDefault(Supplier<String> defaultSupplier) {
        return new OperandParser<>(position, Objects.requireNonNull(defaultSupplier), required, valueMapper, description);
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public ArgumentParsingResult<T> parse(InputArgument[] inputArgs, int index) throws ArgumentParsingException {
        if (inputArgs.length < index)
            throw new IllegalArgumentException("Index " + index + " is out of bounds for argument array of length " + inputArgs.length);
        if (position == index) {
            InputArgument inputArgument = Objects.requireNonNull(inputArgs[index], "Argument at index " + index + " is null");
            inputArgs[index] = null;
            return ArgumentParsingResult.of(mapWithThrows(valueMapper, inputArgument.getValue()));
        }
        // here if not found
        return ArgumentParsingResult.empty();
    }

    @Override
    public ArgumentParsingResult<T> defaultValue() throws ArgumentParsingException {
        if (required)
            return ArgumentParsingResult.empty();

        String defaultStringValue;
        try {
             defaultStringValue = defaultSupplier.get();
        } catch (Exception e) {
            throw new ArgumentParsingException(e);
        }
        return ArgumentParsingResult.of(mapWithThrows(valueMapper, defaultStringValue));
    }

    @Override
    public ParserHelpContent getHelpContent() {
        List<String> indicators = new ArrayList<>();
        if (required)
            indicators.add("required");
        return new ParserHelpContent(List.of("< " + position + ">"), List.of(), indicators, description);
    }

}