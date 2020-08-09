package me.shevtsiv;

import me.shevtsiv.extensions.SuppressExceptionExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.MultipleFailuresError;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class AssertionsTest {

    private Calculator calculator;

    @BeforeEach
    public void initCalculator() {
        this.calculator = new Calculator();
    }

    private boolean isOdd(int num) {
        return num % 2 != 0;
    }

    @Test
    public void assertTrueTest() {
        assertTrue(isOdd(calculator.sumTwoNumbers(2, 3)));
    }

    @Test
    public void assertFalseTest() {
        assertFalse(isOdd(calculator.sumTwoNumbers(2, 2)));
    }

    @Test
    public void assertEqualsTest() {
        assertEquals(4, calculator.sumTwoNumbers(2, 2));
    }

    @Test
    public void assertNotEqualsTest() {
        assertNotEquals(5, calculator.sumTwoNumbers(2, 2));
    }

    @Test
    public void assertSameTest() {
        String first = "foo";
        String second = "foo";
        assertSame(first, second);
        String third = new String("foo"); // deliberately
        assertNotSame(first, third);
        assertNotSame(second, third);
    }

    @Test
    public void assertIterableEqualsTest() {
        assertIterableEquals(List.of(1, 2, 3, 4), List.of(1, 2, 3, 4));
        class BrokenEqualsObject {
            final int id;
            final String name;

            public BrokenEqualsObject(int id, String name) {
                this.id = id;
                this.name = name;
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        }
        var foo = new BrokenEqualsObject(1, "foo");
        var bar = new BrokenEqualsObject(2, "bar");
        // Note the same object are passed, so BrokenEqualsObject#equals is not even invoked here, that's why it passes
        assertIterableEquals(List.of(foo, bar), List.of(foo, bar));

        class ProperEqualsObject {
            final int id;
            final String name;

            public ProperEqualsObject(int id, String name) {
                this.id = id;
                this.name = name;
            }

            @Override
            public boolean equals(Object obj) {
                ProperEqualsObject that = ((ProperEqualsObject) obj);
                return this.id == that.id && this.name.equals(that.name);
            }
        }
        // Note: Different objects have been passed, so ProperEqualsObject#equals has been used for comparison!
        assertIterableEquals(
                List.of(new ProperEqualsObject(1, "foo"), new ProperEqualsObject(2, "bar")),
                List.of(new ProperEqualsObject(1, "foo"), new ProperEqualsObject(2, "bar"))
        );
    }

    @Test
    public void assertLinesMatchTest() {
        assertLinesMatch(List.of("foo", "bar", "baz", "\\d+"), List.of("foo", "bar", "baz", "1000000"));
    }

    @Test
    public void assertAllTest() {
        assertAll(
                () -> assertEquals(4, calculator.sumTwoNumbers(2, 2)),
                () -> assertEquals(5, calculator.sumTwoNumbers(2, 3)),
                () -> assertEquals(2, calculator.sumTwoNumbers(1, 1))
        );
    }

    @Test
    public void assertAllVsMultipleAssertionsPart1Test() {
        AssertionFailedError thrown = assertThrows(AssertionFailedError.class,
                () -> {
                    assertEquals(4, calculator.sumTwoNumbers(2, 2));
                    assertEquals(10_000, calculator.sumTwoNumbers(2, 3)); // Fails
                    // This one would not be even invoked, since the previous one failed, that's why we have
                    // AssertionFailedError as a result with the single failure!
                    assertEquals(999_999, calculator.sumTwoNumbers(1, 1));
                }
        );
        assertEquals(10_000, thrown.getExpected().getValue()); // Not 999_999
        assertEquals(5, thrown.getActual().getValue()); // And not 2
    }

    @Test
    public void assertAllVsMultipleAssertionsPart2Test() {
        MultipleFailuresError thrown = assertThrows(MultipleFailuresError.class,
                () -> assertAll(
                        () -> assertEquals(4, calculator.sumTwoNumbers(2, 2)),
                        () -> assertEquals(10_000, calculator.sumTwoNumbers(2, 3)), // Fails
                        // Although the previous one fails, this would be called anyway, so we get 2 failed assertions
                        // at the end of the test execution!
                        () -> assertEquals(999_999, calculator.sumTwoNumbers(1, 1))
                ));
        assertEquals(2, thrown.getFailures().size());
        AssertionFailedError firstAssertion = (AssertionFailedError) thrown.getFailures().get(0);
        AssertionFailedError secondAssertion = (AssertionFailedError) thrown.getFailures().get(1);
        assertEquals(10_000, firstAssertion.getExpected().getValue());
        assertEquals(5, firstAssertion.getActual().getValue());
        assertEquals(999_999, secondAssertion.getExpected().getValue());
        assertEquals(2, secondAssertion.getActual().getValue());
    }

    @Test
    public void assertDoesNotThrowTest() {
        assertDoesNotThrow(
                () -> 1
        );
        assertThrows(AssertionFailedError.class,
                () -> assertDoesNotThrow(
                        () -> {
                            throw new RuntimeException("Not wanted, but thrown");
                        }
                )
        );
        assertThrows(AssertionFailedError.class,
                () -> assertDoesNotThrow(
                        () -> fail("Not wanted, but thrown")
                )
        );
    }

    @Test
    public void assertTimeoutTest() {
        assertTimeout(Duration.ofMillis(10_000), () -> Thread.sleep(1000));
        assertThrows(AssertionFailedError.class,
                // Executes in the current thread and waits for the end of the execution
                // regardless of the timeout condition.
                () -> assertTimeout(Duration.ofMillis(1000), () -> Thread.sleep(10000))
        );
    }

    @Test
    public void assertTimeoutPreemptivelyTest() {
        assertTimeoutPreemptively(Duration.ofMillis(10_000), () -> Thread.sleep(1000));
        assertThrows(AssertionFailedError.class,
                // Executes in another thread and does not wait until the end of the execution
                // if the timeout condition is already failed.
                () -> assertTimeoutPreemptively(Duration.ofMillis(1000), () -> Thread.sleep(10000))
        );
    }

    @Test
    @ExtendWith(SuppressExceptionExtension.class)
    @Timeout(value = 10, unit = TimeUnit.MILLISECONDS) // Fails preemptively
    public void testAnnotationTimeout() throws InterruptedException {
        Thread.sleep(10_000_000);
    }
}
