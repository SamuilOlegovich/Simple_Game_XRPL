package com.samuilolegovich.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Table(name = "payouts")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payouts {
    @Id // @ID - Важно чтобы была из библиотеке -> javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    private String uuid;
    @Column(name = "user_uuid")
    private String userUuid;
    private String account;
    @Column(name = "destination_tag")
    private String destinationTag;
    @Column(name = "tag_out")
    private String tagOut;
    @Column(name = "available_funds")
    private BigDecimal availableFunds;
    private BigDecimal payouts;
    private BigDecimal bet;
    @Column(name = "datas")
    private String data;
}
