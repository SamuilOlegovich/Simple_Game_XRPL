package com.samuilolegovich.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
