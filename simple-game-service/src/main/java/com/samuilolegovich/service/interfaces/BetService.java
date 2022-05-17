package com.samuilolegovich.service.interfaces;

import com.samuilolegovich.dto.AnswerToBetDto;
import com.samuilolegovich.dto.BetDto;

public interface BetService {
    AnswerToBetDto placeBet(BetDto bet);
}
