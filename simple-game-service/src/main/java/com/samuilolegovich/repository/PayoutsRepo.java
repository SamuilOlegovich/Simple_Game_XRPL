package com.samuilolegovich.repository;

import com.samuilolegovich.domain.Payouts;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayoutsRepo extends CrudRepository<Payouts, Long> {
    Payouts save(Payouts payouts);
}
