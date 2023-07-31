package com.example.microservices.currencyexchangeservice.controller;

import com.example.microservices.currencyexchangeservice.CurrencyExchangeRepository;
import com.example.microservices.currencyexchangeservice.config.CurrencyExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CurrencyExchangeController {
    private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);
    private Environment env;
    private CurrencyExchangeRepository repository;

    @Autowired
    public CurrencyExchangeController(Environment environment, CurrencyExchangeRepository repository) {
        this.env = environment;
        this.repository = repository;
    }

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveCurrencyExchange(@PathVariable String from, @PathVariable String to) {
        CurrencyExchange currencyExchange = repository.findByFromAndTo(from, to);

        logger.info("retrieveExchangeValue called with {} to {}", from, to);

        if (currencyExchange == null)
            throw new RuntimeException("Unable to find data for " + from + " to " + to);

        String port = env.getProperty("local.server.port");

        //CHANGE-KUBERNETES
        String host = env.getProperty("HOSTNAME");
        String version = "v12";

        currencyExchange.setEnvironment(port + " " + version + " " + host);

        return currencyExchange;
    }
}

