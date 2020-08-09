package me.shevtsiv;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderAnnotationOrderedTest {

    @Test
    @Order(1)
    public void first() {
        System.out.println("First number ordered");
    }

    @Test
    @Order(2)
    public void second() {
        System.out.println("Second number ordered");
    }

    @Test
    @Order(3)
    public void third() {
        System.out.println("Third number ordered");
    }

    @Nested
    @TestMethodOrder(MethodOrderer.Alphanumeric.class)
    class AlphanumericOrderedTest {
        @Test
        @Order(3) // Does not work, only method name counts here
        public void a() {
            System.out.println("First alphanumeric");
        }

        @Test
        @Order(2)
        public void b() {
            System.out.println("Second alphanumeric");
        }

        @Test
        @Order(1)
        public void c() {
            System.out.println("Third alphanumeric");
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.Random.class)
    class RandomOrderedTest {
        @Test
        @Order(3) // Does not work, random order works here
        public void a() { // Does not work as well, random order works here
            System.out.println("Maybe first alphanumeric");
        }

        @Test
        @Order(2)
        public void b() {
            System.out.println("Maybe second alphanumeric");
        }

        @Test
        @Order(1)
        public void c() {
            System.out.println("Maybe third alphanumeric");
        }
    }
}
