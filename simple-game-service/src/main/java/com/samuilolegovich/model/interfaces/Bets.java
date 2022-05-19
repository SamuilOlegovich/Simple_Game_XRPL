package com.samuilolegovich.model.interfaces;

import com.samuilolegovich.dto.CommandAnswerDto;
import com.samuilolegovich.dto.UserDto;

public interface Bets {
    CommandAnswerDto calculateTheWin(UserDto userDto);
}
