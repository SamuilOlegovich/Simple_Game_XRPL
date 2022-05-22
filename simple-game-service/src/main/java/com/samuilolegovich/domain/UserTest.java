package com.samuilolegovich.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Table(name = "users_test")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTest {
    @Id // @ID - Важно чтобы была из библиотеке -> javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String uuid;
    private String account;
    @Column(name = "destination_tag")
    private String destinationTag;
    @Column(name = "available_funds")
    private BigDecimal availableFunds;
    private BigDecimal bet;
    @Column(name = "datas")
    private String data;
}
