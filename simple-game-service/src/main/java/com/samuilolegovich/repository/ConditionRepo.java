package com.samuilolegovich.repository;

import com.samuilolegovich.domain.Condition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;


@Repository
public interface ConditionRepo extends CrudRepository<Condition, Long> {
    @Override
    Optional<Condition> findById(Long aLong);
    Optional<Condition> findByBet(BigDecimal bet);
    Condition findById(int id);
}
