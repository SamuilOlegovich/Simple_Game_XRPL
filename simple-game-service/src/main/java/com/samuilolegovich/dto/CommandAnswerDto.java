package com.samuilolegovich.dto;

import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommandAnswerDto {
    private Long id;
    private String uuid;
    private Long baseUserId;
    private String baseUserUuid;
}
