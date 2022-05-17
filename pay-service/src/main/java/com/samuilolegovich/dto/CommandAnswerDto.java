package com.samuilolegovich.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
