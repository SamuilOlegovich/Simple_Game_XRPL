package com.samuilolegovich.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.samuilolegovich.domain.Payouts;

import java.util.Optional;

@Repository
public interface PayoutsRepo extends CrudRepository<Payouts, Long> {
    Optional<Payouts> findByIdAndUuid(Long aLong, String uuid);
    Optional<Payouts> findById(Long aLong);
    void delete(Payouts payouts);
}
