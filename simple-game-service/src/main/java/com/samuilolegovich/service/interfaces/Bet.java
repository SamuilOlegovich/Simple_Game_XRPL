package com.samuilolegovich.service.interfaces;

import com.samuilolegovich.dto.CommandAnswerDto;
import com.samuilolegovich.dto.CommandDto;

public interface Bet {
    CommandAnswerDto placeBet(CommandDto command);
}
