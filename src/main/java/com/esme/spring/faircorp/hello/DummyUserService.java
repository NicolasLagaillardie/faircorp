package com.esme.spring.faircorp.hello;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DummyUserService implements GreetingService {

    @Override
    public void greet(String name) {
        System.out.println("Hello, Spring!");
    }

    public void greetAll() {
        System.out.println(Arrays.asList("Hello, Elodie!", "Hello, Elodie!"));
    }

}
