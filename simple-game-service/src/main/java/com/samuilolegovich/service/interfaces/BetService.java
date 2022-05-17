package com.samuilolegovich.service.interfaces;

import com.samuilolegovich.dto.AnswerToBetDto;
import com.samuilolegovich.dto.BetDto;
import com.samuilolegovich.dto.CommandDto;

public interface BetService {
    AnswerToBetDto placeBet(CommandDto command);
}
