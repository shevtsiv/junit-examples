package me.shevtsiv;

import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class TestFactoryTest {

    @TestFactory
    public Stream<DynamicTest> dynamicTestExample() {
        return Stream
                .of(1, 3, 5, 7, 9, 11)
                .map(number -> dynamicTest("Is " + number + " odd?", () -> assertNotEquals(0, number & 1)));
    }

    @TestFactory
    public Stream<DynamicContainer> dynamicTestWithMultipleContainersExample() {
        return Stream.of(
                dynamicContainer(
                        "odd numbers",
                        Stream.of(1, 3, 5)
                                .map(oddNumber -> dynamicTest(
                                        "Is " + oddNumber + " odd?",
                                        () -> assertNotEquals(0, oddNumber & 1)
                                ))
                ),
                dynamicContainer(
                        "even numbers",
                        Stream.of(0, 2, 4, 6)
                                .map(evenNumber -> dynamicTest(
                                        "Is " + evenNumber + " even?",
                                        () -> assertEquals(0, evenNumber & 1)
                                ))
                )
        );
    }
}
