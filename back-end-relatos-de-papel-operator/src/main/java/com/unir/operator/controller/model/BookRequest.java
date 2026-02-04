package com.unir.operator.controller.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    @NotNull(message = "`id` cannot be null")
    @Positive(message = "`id` must be integer greater than 0")
    private Integer ID;
    @NotNull(message = "`quantity` cannot be null")
    @Positive(message = "`id` must be integer greater than 0")
    private Integer quantity;

}
