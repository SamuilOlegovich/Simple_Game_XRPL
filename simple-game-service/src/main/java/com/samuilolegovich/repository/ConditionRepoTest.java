package com.samuilolegovich.repository;

import com.samuilolegovich.domain.ConditionTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ConditionRepoTest extends CrudRepository<ConditionTest, Long> {
    @Override
    Optional<ConditionTest> findById(Long aLong);

    Optional<ConditionTest> findByBet(BigDecimal bet);
    ConditionTest findById(int id);
}
