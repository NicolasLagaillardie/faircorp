package com.emse.spring.faircorp.hello;

import com.esme.spring.faircorp.hello.ConsoleGreetingService;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import org.springframework.boot.test.rule.OutputCapture;

public class ConsoleGreetingServiceTests {

    @Rule
    public OutputCapture outputCapture = new OutputCapture();

    @Test
    public void testGreeting() {
        ConsoleGreetingService greetingService = new ConsoleGreetingService();
        greetingService.greet("Spring");
        outputCapture.expect(Matchers.startsWith("Hello, Spring!"));
    }
}