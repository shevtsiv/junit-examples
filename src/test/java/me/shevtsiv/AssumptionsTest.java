package me.shevtsiv;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

public class AssumptionsTest {

    @Test
    public void assumeTrueEvaluatesToFalseAborted() {
        assumeTrue(false, "This test would be aborted and the following lines won't be executed!");
        fail("You WILL NOT see this message as a test result, because this line is not executed!");
    }

    @Test
    public void assumeFalseEvaluatesToTrueAborted() {
        assumeFalse(true, "This test would be aborted and the following lines won't be executed!");
        fail("You WILL NOT see this message as a test result, because this line is not executed!");
    }

    @Test
    public void assumeTrueEvaluatesToTrue() {
        assumeTrue(true, "This test WILL NOT be aborted and the following lines WILL be executed!");
        String testFailureMessage = "You WILL see this message as a test result, because this line is not executed!";
        AssertionFailedError assertionFailedError = assertThrows(
                AssertionFailedError.class,
                () -> fail(testFailureMessage)
        );
        assertEquals(testFailureMessage, assertionFailedError.getMessage());
    }

    @Test
    public void assumingThatTest() {
        List<Integer> numbers = List.of(1, 2, 3, 5, 6, 7, 8, 9, 10);
        for (int number : numbers) {
            assertTrue(number > 0);
            assumingThat(number == 10, () -> assertEquals(0, number & 1));
        }
    }
}
