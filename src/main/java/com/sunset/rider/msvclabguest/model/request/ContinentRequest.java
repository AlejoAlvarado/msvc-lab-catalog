package com.sunset.rider.msvclabguest.model.request;

import com.sunset.rider.msvclabguest.model.Continent;
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
public class ContinentRequest {
    @NotNull(message = "continent no puede ser nulo")
    private String name;
}
