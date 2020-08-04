package me.shevtsiv.parametrized;

import me.shevtsiv.Calculator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ArgumentProviderTest {

    private static List<Integer> provideListOfSingleIntegers() {
        return List.of(1, 3, 5, 7, 9);
    }

    @ParameterizedTest
    @MethodSource("provideListOfSingleIntegers")
    public void provideListOfSingleIntegersTest(Integer intParameter) {
        assertNotEquals(0, intParameter % 2);
    }

    @ParameterizedTest
    @MethodSource // Note: Test method name is the same as provider method name, so no explicit declaration
    public void provideListOfSingleIntegers(Integer intParameter) {
        assertNotEquals(0, intParameter % 2);
    }

    @ParameterizedTest
    @MethodSource("me.shevtsiv.parametrized.AnotherClassWithMethodProvider#provideListOfSingleIntegersFromAnotherClass")
    public void provideListOfSingleIntegersFromAnotherClass(Integer intParameter) {
        assertEquals(0, intParameter % 2);
    }

    @ParameterizedTest
    @ArgumentsSource(RandomPairOfIntegersArgumentProvider.class)
    public void customInfiniteRandomPairOfIntegersArgumentProvider(int a, int b) {
        Calculator calculator = new Calculator();
        assertEquals(a + b, calculator.sumTwoNumbers(a, b));
    }
}

@SuppressWarnings("unused") // Used by ArgumentProviderTest#provideListOfSingleIntegersFromAnotherClass
class AnotherClassWithMethodProvider {
    private static List<Integer> provideListOfSingleIntegersFromAnotherClass() {
        return List.of(0, 2, 4, 6, 8);
    }
}

class RandomPairOfIntegersArgumentProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        Random random = new Random();
        return Stream.of(
                Arguments.of(random.nextInt(), random.nextInt()),
                Arguments.of(random.nextInt(), random.nextInt()),
                Arguments.of(random.nextInt(), random.nextInt())
        );
    }
}
