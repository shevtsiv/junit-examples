package me.shevtsiv.extensions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BenchmarkExtension.class, TestMethodOnlyBenchmarkExtension.class})
public class BenchmarkExtensionUsageTest {

    @BeforeAll
    public static void thisMethodExecutionTimeIsNotCountedByAnyOfExtensions() throws InterruptedException {
        Thread.sleep(10);
    }

    @BeforeAll
    public static void thisMethodExecutionTimeIsNotCountedByAnyOfExtensionsAsWell() throws InterruptedException {
        Thread.sleep(10);
    }

    @BeforeEach
    public void benchmarkExtensionCountsThisMethodExecutionTime() throws InterruptedException {
        Thread.sleep(10);
    }

    @AfterEach
    public void benchmarkExtensionCountsThisMethodExecutionTimeAsWell() throws InterruptedException {
        Thread.sleep(10);
    }

    @Test
    public void testExecutionTime() throws InterruptedException {
        Thread.sleep(10);
    }

    @Test
    public void emptyTestExecutionTime() {
    }
}
