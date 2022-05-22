package com.samuilolegovich.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Table(name = "conditions_test")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConditionTest {
    @Id // @ID - Важно чтобы была из библиотеке -> javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    private BigDecimal bet;
    // доп смещении для генератора
    private Integer bias;
}
