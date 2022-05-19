package com.samuilolegovich.service.interfaces;

import com.samuilolegovich.dto.CommandAnswerDto;
import com.samuilolegovich.dto.CommandDto;

public interface BetService {
    CommandAnswerDto placeBet(CommandDto command);
}
