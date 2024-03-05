package com.sunset.rider.msvclabguest.handler;

import com.sunset.rider.msvclabguest.model.Continent;
import com.sunset.rider.msvclabguest.model.Country;
import com.sunset.rider.msvclabguest.model.request.ContinentRequest;
import com.sunset.rider.msvclabguest.model.request.CountryRequest;
import com.sunset.rider.msvclabguest.service.ContinentService;
import com.sunset.rider.msvclabguest.utils.ErrorNotFound;
import io.micrometer.common.util.StringUtils;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
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

@Component
public class ContinentHandler {
    private ContinentService continentService;
    private Validator validator;

    public ContinentHandler(ContinentService continentService, Validator validator){
        this.validator = validator;
        this.continentService = continentService;
    }
    public Mono<ServerResponse> findContinents(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(continentService.findAll(), Country.class);
    }

    public Mono<ServerResponse> findContinentById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return continentService.findById(id)
                .flatMap(country -> ServerResponse
                        .ok().contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(country)))
                .switchIfEmpty(ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .body(BodyInserters.fromValue(ErrorNotFound.error(id))));
    }

    public Mono<ServerResponse> saveContinent(ServerRequest request) {
        Mono<ContinentRequest> guestRequest = request.bodyToMono(ContinentRequest.class);

        return guestRequest
                .flatMap(rq -> {
                    Errors errors = new BeanPropertyBindingResult(rq, ContinentRequest.class.getName());
                    validator.validate(rq, errors);


                    if (errors.hasErrors()) {
                        Map<String, Object> erroresMap = new HashMap<>();
                        List<String> errorList = new ArrayList<>();
                        errors.getFieldErrors().forEach(e -> errorList.add(e.getDefaultMessage()));
                        erroresMap.put("errores", errorList);
                        erroresMap.put("timestamp", LocalDateTime.now());

                        return ServerResponse.badRequest().body(BodyInserters.fromValue(erroresMap));
                    } else {
                        return continentService.save(buildContinent(rq, null,null))
                                .flatMap(room -> ServerResponse.created(URI.create("/guests/".concat(room.getId())))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromValue(room)));
                    }

                })
                .onErrorResume(error -> {
                    WebClientResponseException errorResponse = (WebClientResponseException) error;

                    return Mono.error(errorResponse);
                });
    }

    public Mono<ServerResponse> updateContinent(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<ContinentRequest> guestRequestMono = serverRequest.bodyToMono(ContinentRequest.class);

        return continentService.findById(id)
                .flatMap(continent ->
                {

                    Errors errors = new BeanPropertyBindingResult(guestRequestMono, CountryRequest.class.getName());
                    validator.validate(guestRequestMono, errors);


                    if (errors.hasErrors()) {
                        Map<String, Object> erroresMap = new HashMap<>();
                        List<String> errorList = new ArrayList<>();
                        errors.getFieldErrors().forEach(e -> errorList.add(e.getDefaultMessage()));
                        erroresMap.put("errores", errorList);

                        return ServerResponse.badRequest().body(BodyInserters.fromValue(erroresMap));
                    } else {
                        return guestRequestMono
                                .flatMap(rq -> continentService.update(buildContinent(rq, id, continent)))
                                .flatMap(roomDb -> ServerResponse
                                        .created(URI.create("/guests/".concat(roomDb.getId())))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromValue(roomDb)));
                    }

                })
                .switchIfEmpty(ServerResponse
                        .status(HttpStatus.NOT_FOUND)
                        .body(BodyInserters.fromValue(ErrorNotFound.error(id))))
                .onErrorResume(error -> {
                    WebClientResponseException errorResponse = (WebClientResponseException) error;

                    return Mono.error(errorResponse);
                });
    }

    public Mono<ServerResponse> deleteContinent(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return continentService.delete(id)
                .then(ServerResponse.noContent().build());
    }

    private Continent buildContinent(ContinentRequest continentRequest, String id, Continent country) {

        return Continent.builder()
                .id(StringUtils.isEmpty(id) ? null : id)
                .name(continentRequest.getName())
                .createdAt(StringUtils.isEmpty(id) ? LocalDateTime.now() : country.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

    }
}
