package me.shevtsiv.parametrized;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArgumentAggregatorTest {

    static class Position {
        int x;
        int y;
        int z;

        public Position(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public boolean containsZeroCoordinate() {
            return x == 0 || y == 0 || z == 0;
        }
    }

    @ParameterizedTest
    @CsvSource({"0,0,0", "3,0,0", "0,3,0", "0,0,3"})
    public void aggregateToPositionViaRawArgAccessor(ArgumentsAccessor argumentsAccessor) {
        int x = argumentsAccessor.getInteger(0);
        int y = argumentsAccessor.getInteger(1);
        int z = argumentsAccessor.getInteger(2);
        Position position = new Position(x, y, z);
        assertTrue(position.containsZeroCoordinate());
    }

    static class PositionAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) {
            int x = accessor.getInteger(0);
            int y = accessor.getInteger(1);
            int z = accessor.getInteger(2);
            return new Position(x, y, z);
        }
    }

    @ParameterizedTest
    @CsvSource({"0,0,0", "3,0,0", "0,3,0", "0,0,3"})
    public void aggregateToPositionViaArgAggregator(
            @AggregateWith(PositionAggregator.class) Position position
    ) {
        assertTrue(position.containsZeroCoordinate());
    }
}
