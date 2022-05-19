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

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    // указываем как у нас будет сохранятся дата при сериализации
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
