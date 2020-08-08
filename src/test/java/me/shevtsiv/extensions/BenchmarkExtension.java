package me.shevtsiv.extensions;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class BenchmarkExtension implements BeforeEachCallback, AfterEachCallback {

    private long startTime;

    @Override
    public void beforeEach(ExtensionContext context) {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        System.out.println(
                "Execution time of " + context.getDisplayName() + " with BeforeEach and AfterEach methods" +
                        " is " + (System.currentTimeMillis() - startTime) + " milliseconds!"
        );
    }
}
