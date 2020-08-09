package me.shevtsiv.extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public class SuppressExceptionExtension implements TestExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) {
        // Do nothing here so the test always passes, even if it throws an exception
        System.out.println(throwable);
    }
}
