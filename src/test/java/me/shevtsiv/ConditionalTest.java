package me.shevtsiv;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.condition.OS.LINUX;
import static org.junit.jupiter.api.condition.OS.MAC;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

public class ConditionalTest {

    @Test
    @EnabledOnOs({ LINUX, MAC, WINDOWS })
    public void testOnMostPopularOS() {
        assertTrue(List.of(LINUX, MAC, WINDOWS).stream().anyMatch(OS::isCurrentOs));
    }

    @Test
    @EnabledOnJre(JRE.JAVA_9)
    public void testOnJre9Only() {
        assertTrue(System.getProperty("java.version").startsWith("1.9"));
    }

    @Test
    @EnabledOnJre(JRE.JAVA_11)
    public void testOnJre11Only() {
        assertTrue(System.getProperty("java.version").startsWith("11"));
    }

    @Test
    @EnabledOnJre(JRE.JAVA_14)
    public void testOnJre14Only() {
        assertTrue(System.getProperty("java.version").startsWith("14"));
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "8")
    public void testOn8ProcessorsOnlyViaEnvironmentVariables() {
        assertEquals("8", System.getenv("NUMBER_OF_PROCESSORS"));
    }
}
