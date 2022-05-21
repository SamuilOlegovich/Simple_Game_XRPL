package com.samuilolegovich.repository;

import com.samuilolegovich.domain.PayoutsTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayoutsRepoTest extends CrudRepository<PayoutsTest, Long> {
    Optional<PayoutsTest> findByIdAndUuid(Long aLong, String uuid);
    Optional<PayoutsTest> findById(Long aLong);
    void delete(PayoutsTest payouts);
    List<PayoutsTest> findAll();
}
