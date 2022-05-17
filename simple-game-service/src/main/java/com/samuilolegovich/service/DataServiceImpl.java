package com.samuilolegovich.service;

import com.samuilolegovich.dto.AnswerToBetDto;
import com.samuilolegovich.repository.LottoRepo;
import com.samuilolegovich.service.interfaces.DataService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DataServiceImpl implements DataService {
    private final LottoRepo lottoRepo;

    public AnswerToBetDto getDataFoThisPage() {
        Long lottoNow = lottoRepo.findFirstByOrderByCreatedAtDesc().getTotalLottoCredits();
        return AnswerToBetDto.builder().lottoNow(lottoNow).build();
    }
}
