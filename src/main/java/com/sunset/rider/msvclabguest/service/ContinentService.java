package com.sunset.rider.msvclabguest.service;

import com.sunset.rider.msvclabguest.model.Continent;
import com.sunset.rider.msvclabguest.model.Country;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ContinentService {
    Flux<Continent> findAll();

    Mono<Continent> findById(String id);

    Mono<Continent> save(Continent continent);

    Mono<Continent> update(Continent continent);

    Mono<Void> delete(String id);
}
