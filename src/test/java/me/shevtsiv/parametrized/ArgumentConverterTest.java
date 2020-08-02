package me.shevtsiv.parametrized;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArgumentConverterTest {

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

    static class StringToPositionConverter implements ArgumentConverter {

        @Override
        public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
            if (!(source instanceof String)) {
                throw new RuntimeException("Invalid source type provided for StringToPositionConverter");
            }
            String sourceString = (String) source;
            String[] coordinates = sourceString.split("/");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            int z = Integer.parseInt(coordinates[2]);
            return new Position(x, y, z);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"0/0/0", "3/0/0", "0/3/0", "0/0/3"})
    public void slashedStringToPositionConversion(
            @ConvertWith(StringToPositionConverter.class) Position position
    ) {
        assertTrue(position.containsZeroCoordinate());
    }
}
