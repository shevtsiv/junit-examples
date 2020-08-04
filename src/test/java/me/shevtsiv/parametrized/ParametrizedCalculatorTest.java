package me.shevtsiv.parametrized;

import me.shevtsiv.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParametrizedCalculatorTest {

    private Calculator calculator;

    @BeforeEach
    public void initCalculator() {
        this.calculator = new Calculator();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    public void intsValueSource(int parameter) {
        assertEquals(parameter + 1, calculator.sumTwoNumbers(parameter, 1));
    }

    @ParameterizedTest(name = "{index}) sum of {0} and {1} is {2}")
    @CsvSource({"1,1,2", "1,2,3", "2,3,5"})
    public void intsParametrizedTest(int a, int b, int expectedSum) {
        assertEquals(expectedSum, calculator.sumTwoNumbers(a, b));
    }

    @ParameterizedTest
    // Test file resides is in a /build directory, so this test requires project building first
    @CsvFileSource(resources = "../../../../../resources/test/sumOfTwo.csv", numLinesToSkip = 1)
    public void csvIntsFromFile(int a, int b, int expectedSum) {
        assertEquals(expectedSum, calculator.sumTwoNumbers(a, b));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void nullAndEmptyValues(String a) {
        assertThrows(
                NumberFormatException.class,
                () -> calculator.sumTwoNumbers(Integer.parseInt(a), Integer.parseInt(a))
        );
    }

    enum SumOfTwo {
        FOUR(2, 2, 4),
        FIVE(2, 3, 5),
        SIX(3, 3, 6);

        private final int a;
        private final int b;
        private final int result;

        SumOfTwo(int a, int b, int result) {
            this.a = a;
            this.b = b;
            this.result = result;
        }

        public int getA() {
            return a;
        }

        public int getB() {
            return b;
        }

        public int getResult() {
            return result;
        }
    }

    @ParameterizedTest
    @EnumSource
    public void enumValues(SumOfTwo sumOfTwo) {
        assertEquals(
                sumOfTwo.getResult(),
                calculator.sumTwoNumbers(sumOfTwo.getA(), sumOfTwo.getB())
        );
    }

    @ParameterizedTest
    @EnumSource(value = SumOfTwo.class, names = "SIX")
    public void enumValuesInclusively(SumOfTwo sumOfTwo) {
        assertEquals(6, sumOfTwo.getResult());
        assertEquals(
                sumOfTwo.getResult(),
                calculator.sumTwoNumbers(sumOfTwo.getA(), sumOfTwo.getB())
        );
    }

    @ParameterizedTest
    @EnumSource(value = SumOfTwo.class, names = {"FIVE", "SIX"}, mode = EnumSource.Mode.EXCLUDE)
    public void enumValuesExclusively(SumOfTwo sumOfTwo) {
        assertEquals(SumOfTwo.FOUR, sumOfTwo); // Only FOUR left
        assertEquals(
                sumOfTwo.getResult(),
                calculator.sumTwoNumbers(sumOfTwo.getA(), sumOfTwo.getB())
        );
    }

    @ParameterizedTest
    @EnumSource(names = ".+IVE", mode = EnumSource.Mode.MATCH_ANY)
    public void enumValuesInclusivelyByRegExpMatchAny(SumOfTwo sumOfTwo) {
        assertEquals(5, sumOfTwo.getResult());
        assertEquals(
                sumOfTwo.getResult(),
                calculator.sumTwoNumbers(sumOfTwo.getA(), sumOfTwo.getB())
        );
    }

    @ParameterizedTest
    @EnumSource(value = SumOfTwo.class, names = {"(FO)..", "..(UR)"}, mode = EnumSource.Mode.MATCH_ALL)
    public void enumValuesInclusivelyByRegExpMatchAll(SumOfTwo sumOfTwo) {
        assertEquals(SumOfTwo.FOUR, sumOfTwo);
        assertEquals(
                sumOfTwo.getResult(),
                calculator.sumTwoNumbers(sumOfTwo.getA(), sumOfTwo.getB())
        );
    }
}
