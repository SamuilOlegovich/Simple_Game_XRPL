package com.samuilolegovich.repository;

import com.samuilolegovich.domain.LottoTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LottoRepoTest extends CrudRepository<LottoTest, Long> {
    LottoTest findFirstByOrderByCreatedAtDesc();
}
