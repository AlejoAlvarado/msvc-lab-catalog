package com.sunset.rider.msvclabguest.service;

import com.sunset.rider.msvclabguest.model.Country;
import com.sunset.rider.msvclabguest.repository.CountryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CountryServiceImpl implements CountryService {

    private CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository){
        this.countryRepository = countryRepository;
    }

    @Override
    public Flux<Country> findAll() {
        return countryRepository.findAll();
    }

    @Override
    public Mono<Country> findById(String id) {
        return countryRepository.findById(id);
    }

    @Override
    public Flux<Country> findCountriesByContinentName(String name) {
        return countryRepository.findCountriesByContinentName(name);
    }

    @Override
    public Mono<Country> save(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public Mono<Country> update(Country country) {
        return countryRepository.save(country);
    }

    @Override
    public Mono<Void> delete(String id) {
        return countryRepository.deleteById(id);
    }
}
