package com.example.microservices.currencyconversionservice.controller;

import com.example.microservices.currencyconversionservice.CurrencyConversionBean;
//import com.example.microservices.currencyconversionservice.CurrencyExchangeServiceProxy;
import com.example.microservices.currencyconversionservice.CurrencyExchangeProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConversionController {
    private Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);
    CurrencyExchangeProxy proxy;
    private RestTemplate restTemplate;

    @Autowired
    public CurrencyConversionController(RestTemplate restTemplate, CurrencyExchangeProxy proxy) {
        this.restTemplate = restTemplate;
        this.proxy = proxy;
    }

    // v1 - without Feign
    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrency(
        @PathVariable String from,
        @PathVariable String to,
        @PathVariable BigDecimal quantity
    ) {
        //CHANGE-KUBERNETES
        logger.info("calculateCurrencyConversion called with {} to {} with {}", from, to, quantity);

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        ResponseEntity<CurrencyConversionBean> responseEntity =
            restTemplate.getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class, uriVariables);

        CurrencyConversionBean response = responseEntity.getBody();

        return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(),
            quantity, quantity.multiply(response.getConversionMultiple()), response.getEnvironment() + "-Rest template");
    }

    // v2 - Feign
    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrencyFeign(
        @PathVariable String from,
        @PathVariable String to,
        @PathVariable BigDecimal quantity
    ) {
        //CHANGE-KUBERNETES
        logger.info("calculateCurrencyConversion called with {} to {} with {}", from, to, quantity);

        CurrencyConversionBean response = proxy.retrieveCurrencyExchange(from, to);

        return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(),
            quantity, quantity.multiply(response.getConversionMultiple()), response.getEnvironment() + "-Feign");
    }
}
