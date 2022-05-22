package com.samuilolegovich.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import javax.persistence.*;

@Data
@Table(name = "lotto")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lotto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "total_lotto")
    private BigDecimal totalLotto;
}
