package com.samuilolegovich.dto;

import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String uuid;
    private String account;
    private String destinationTag;
    private BigDecimal availableFunds;
    private BigDecimal bet;
    private String data;
}
