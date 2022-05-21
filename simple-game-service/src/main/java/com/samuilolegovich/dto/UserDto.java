package com.samuilolegovich.dto;

import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private Long userId;
    private String uuid;
    private String userUuid;
    private String account;
    private String destinationTag;
    private BigDecimal availableFunds;
    private BigDecimal bet;
    private String data;
}
