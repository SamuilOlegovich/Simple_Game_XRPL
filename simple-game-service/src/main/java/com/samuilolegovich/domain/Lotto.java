package com.samuilolegovich.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
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
    private BigDecimal replenishment;
    @Column(name = "total_lotto")
    private BigDecimal totalLotto;

    @CreationTimestamp
    // указываем что поле не обновляемое
    @Column(name = "created_at", updatable = false)
    // указываем как у нас будет сохранятся дата при сериализации
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
