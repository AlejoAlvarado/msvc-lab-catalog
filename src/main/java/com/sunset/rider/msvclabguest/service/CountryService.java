package com.sunset.rider.msvclabguest.service;

import com.sunset.rider.msvclabguest.model.Country;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CountryService {
    Flux<Country> findAll();

    Mono<Country> findById(String id);

    Flux<Country> findCountriesByContinentName(String name);

    Mono<Country> save(Country country);

    Mono<Country> update(Country country);

    Mono<Void> delete(String id);
}
