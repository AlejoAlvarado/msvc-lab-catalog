package com.sunset.rider.msvclabguest.service;

import com.sunset.rider.msvclabguest.model.Continent;
import com.sunset.rider.msvclabguest.model.Country;
import com.sunset.rider.msvclabguest.repository.ContinentRepository;
import com.sunset.rider.msvclabguest.repository.CountryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ContinentServiceImpl implements ContinentService {

  private ContinentRepository continentRepository;

  public ContinentServiceImpl(ContinentRepository continentRepository) {
    this.continentRepository = continentRepository;
  }

  @Override
  public Flux<Continent> findAll() {
    return continentRepository.findAll();
  }

  @Override
  public Mono<Continent> findById(String id) {
    return continentRepository.findById(id);
  }

  @Override
  public Mono<Continent> save(Continent continent) {
    return continentRepository.save(continent);
  }

  @Override
  public Mono<Continent> update(Continent continent) {
    return continentRepository.save(continent);
  }

  @Override
  public Mono<Void> delete(String id) {
    return continentRepository.deleteById(id);
  }
}
