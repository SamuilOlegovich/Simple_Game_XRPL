package com.samuilolegovich.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Table(name = "lotto_test")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LottoTest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "total_lotto")
    private BigDecimal totalLotto;
}
