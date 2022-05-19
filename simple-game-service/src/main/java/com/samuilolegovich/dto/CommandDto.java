package com.samuilolegovich.dto;

import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommandDto {
    private Long id;
    private String uuid;
}
