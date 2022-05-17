package com.samuilolegovich.dto;

import com.samuilolegovich.enums.Enums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BetDto {
    private Long id;
    private String uuid;
    private String account;
    private String destinationTag;
    private BigDecimal availableFunds;
    private BigDecimal bet;
    private Enums colorBet;
    private Long data;
}
