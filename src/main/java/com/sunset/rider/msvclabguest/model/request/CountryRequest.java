package com.sunset.rider.msvclabguest.model.request;

import com.sunset.rider.msvclabguest.model.Continent;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
public class CountryRequest {
    @NotNull(message = "continent no puede ser nulo")
    private Continent continent;
    @NotEmpty(message = "name no puede ser vacio o nulo")
    private String name;
    @NotEmpty(message = "prefix no puede ser vacio o nulo")
    private String prefix;
}
