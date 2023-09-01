package io.github.johannesbuchholz.clihats.core.execution.parser;

import io.github.johannesbuchholz.clihats.core.execution.AbstractParser;

import java.util.Optional;

public abstract class AbstractOperandParser extends AbstractParser {

    public abstract int getPosition();

    @Override
    public String getDisplayName() {
        return "<" + getPosition() + ">";
    }

    @Override
    public String toString() {
        return "Operand " + getPosition();
    }

    @Override
    protected int getParsingPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected Optional<String> getConflictMessage(AbstractParser other) {
        if (!(other instanceof AbstractOperandParser))
            return Optional.empty();
        if (getPosition() == ((AbstractOperandParser) other).getPosition())
            return Optional.of("Operands conflict on position " + getPosition());
        return Optional.empty();
    }

}
