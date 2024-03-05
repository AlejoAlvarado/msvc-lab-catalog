package com.sunset.rider.msvclabguest.repository;

import com.sunset.rider.msvclabguest.model.Country;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CountryRepository extends ReactiveMongoRepository<Country,String> {
    @Query("{continent.name: ?0}")
    Flux<Country> findCountriesByContinentName(String name);
}
