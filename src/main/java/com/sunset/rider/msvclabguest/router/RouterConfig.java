package com.sunset.rider.msvclabguest.router;

import com.sunset.rider.msvclabguest.handler.ContinentHandler;
import com.sunset.rider.msvclabguest.handler.CountryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {
  @Bean
  public RouterFunction<ServerResponse> rutasCountry(CountryHandler countryHandler) {
    return RouterFunctions.route(RequestPredicates.GET("/countries"), countryHandler::findCountries)
        .andRoute(RequestPredicates.GET("/countries/{id}"), countryHandler::findCountryById)
        .andRoute(RequestPredicates.POST("/countries"), countryHandler::saveCountry)
        .andRoute(RequestPredicates.PUT("/countries/{id}"), countryHandler::updateCountry)
        .andRoute(RequestPredicates.DELETE("/countries/{id}"), countryHandler::deleteCountry)
        .andRoute(
            RequestPredicates.GET("/countries/continent/{name}"),
            countryHandler::findCountriesByContinentName);
  }

  @Bean
  public RouterFunction<ServerResponse> rutasContinent(ContinentHandler continentHandler) {
    return RouterFunctions.route(
            RequestPredicates.GET("/continents"), continentHandler::findContinents)
        .andRoute(RequestPredicates.GET("/continents/{id}"), continentHandler::findContinentById)
        .andRoute(RequestPredicates.POST("/continents"), continentHandler::saveContinent)
        .andRoute(RequestPredicates.PUT("/continents/{id}"), continentHandler::updateContinent)
        .andRoute(RequestPredicates.DELETE("/continents/{id}"), continentHandler::deleteContinent);
  }
}
