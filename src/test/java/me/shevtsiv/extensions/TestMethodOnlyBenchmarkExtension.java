package me.shevtsiv.extensions;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestMethodOnlyBenchmarkExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private long startTime;

    @Override
    public void afterTestExecution(ExtensionContext context) {
        System.out.println(
                "Execution time of " + context.getDisplayName() + " only" +
                        " is " + (System.currentTimeMillis() - startTime) + " milliseconds!"
        );
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        startTime = System.currentTimeMillis();
    }
}
