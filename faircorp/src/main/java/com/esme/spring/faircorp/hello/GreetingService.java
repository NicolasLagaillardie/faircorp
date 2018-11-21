package com.esme.spring.faircorp.hello;

import org.springframework.stereotype.Service;

@Service
public interface GreetingService {

    void greet(String name);

}