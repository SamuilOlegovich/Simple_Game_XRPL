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
    private String userId;
//    private String userName;
    private String userAddress;
    private Enums colorBet;
    private BigDecimal bet;
    private BigDecimal availableFunds;
    private long data;
}
