package com.samuilolegovich.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import javax.persistence.*;

@Data
@Table(name = "conditions")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Condition {
    @Id // @ID - Важно чтобы была из библиотеке -> javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    private BigDecimal bet;
    // доп смещении для генератора
    private Integer bias;
}
