package com.rawan.currencyconversionservice;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="currency-exchange-service")  
@EnableDiscoveryClient
public interface CurrencyExchangeServiceProxy {

    @GetMapping("/currency-exchange/from/{from}/to/{to}") // where {from} and {to} are path variable
    public CurrencyConversionBean retrieveExchangeValue(@PathVariable String from, @PathVariable String to); // from map to USD

}
