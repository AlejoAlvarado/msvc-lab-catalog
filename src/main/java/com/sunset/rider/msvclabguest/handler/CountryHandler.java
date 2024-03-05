package com.sunset.rider.msvclabguest.handler;

import com.sunset.rider.msvclabguest.model.Country;
import com.sunset.rider.msvclabguest.model.request.CountryRequest;
import com.sunset.rider.msvclabguest.service.CountryService;
import com.sunset.rider.msvclabguest.utils.ErrorNotFound;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class CountryHandler {
  private CountryService countryService;
  private Validator validator;

  public CountryHandler(CountryService countryService, Validator validator) {
    this.validator = validator;
    this.countryService = countryService;
  }

  public Mono<ServerResponse> findCountries(ServerRequest serverRequest) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(countryService.findAll(), Country.class);
  }

  public Mono<ServerResponse> findCountryById(ServerRequest serverRequest) {
    String id = serverRequest.pathVariable("id");

    return countryService
        .findById(id)
        .flatMap(
            country ->
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(country)))
        .switchIfEmpty(
            ServerResponse.status(HttpStatus.NOT_FOUND)
                .body(BodyInserters.fromValue(ErrorNotFound.error(id))));
  }

  public Mono<ServerResponse> findCountriesByContinentName(ServerRequest serverRequest) {
    String name = serverRequest.pathVariable("name");

    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(countryService.findCountriesByContinentName(name), Country.class);
  }

  public Mono<ServerResponse> saveCountry(ServerRequest request) {
    Mono<CountryRequest> guestRequest = request.bodyToMono(CountryRequest.class);

    return guestRequest
        .flatMap(
            rq -> {
              Errors errors = new BeanPropertyBindingResult(rq, CountryRequest.class.getName());
              validator.validate(rq, errors);

              if (errors.hasErrors()) {
                Map<String, Object> erroresMap = new HashMap<>();
                List<String> errorList = new ArrayList<>();
                errors.getFieldErrors().forEach(e -> errorList.add(e.getDefaultMessage()));
                erroresMap.put("errores", errorList);
                erroresMap.put("timestamp", LocalDateTime.now());

                return ServerResponse.badRequest().body(BodyInserters.fromValue(erroresMap));
              } else {
                return countryService
                    .save(buildCountry(rq, null, null))
                    .flatMap(
                        room ->
                            ServerResponse.created(URI.create("/guests/".concat(room.getId())))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(room)));
              }
            })
        .onErrorResume(
            error -> {
              WebClientResponseException errorResponse = (WebClientResponseException) error;

              return Mono.error(errorResponse);
            });
  }

  public Mono<ServerResponse> updateCountry(ServerRequest serverRequest) {
    String id = serverRequest.pathVariable("id");
    Mono<CountryRequest> guestRequestMono = serverRequest.bodyToMono(CountryRequest.class);

    return countryService
        .findById(id)
        .flatMap(
            country -> {
              Errors errors =
                  new BeanPropertyBindingResult(guestRequestMono, CountryRequest.class.getName());
              validator.validate(guestRequestMono, errors);

              if (errors.hasErrors()) {
                Map<String, Object> erroresMap = new HashMap<>();
                List<String> errorList = new ArrayList<>();
                errors.getFieldErrors().forEach(e -> errorList.add(e.getDefaultMessage()));
                erroresMap.put("errores", errorList);

                return ServerResponse.badRequest().body(BodyInserters.fromValue(erroresMap));
              } else {
                return guestRequestMono
                    .flatMap(rq -> countryService.update(buildCountry(rq, id, country)))
                    .flatMap(
                        roomDb ->
                            ServerResponse.created(URI.create("/guests/".concat(roomDb.getId())))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(roomDb)));
              }
            })
        .switchIfEmpty(
            ServerResponse.status(HttpStatus.NOT_FOUND)
                .body(BodyInserters.fromValue(ErrorNotFound.error(id))))
        .onErrorResume(
            error -> {
              WebClientResponseException errorResponse = (WebClientResponseException) error;

              return Mono.error(errorResponse);
            });
  }

  public Mono<ServerResponse> deleteCountry(ServerRequest serverRequest) {
    String id = serverRequest.pathVariable("id");

    return countryService.delete(id).then(ServerResponse.noContent().build());
  }

  public Country buildCountry(CountryRequest countryRequest, String id, Country country) {

    return Country.builder()
        .id(StringUtils.isEmpty(id) ? null : id)
        .continent(countryRequest.getContinent())
        .name(countryRequest.getName())
        .prefix(countryRequest.getPrefix())
        .createdAt(StringUtils.isEmpty(id) ? LocalDateTime.now() : country.getCreatedAt())
        .updatedAt(LocalDateTime.now())
        .build();
  }
}
