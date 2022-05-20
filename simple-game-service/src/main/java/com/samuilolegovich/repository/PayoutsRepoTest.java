package com.samuilolegovich.repository;

import com.samuilolegovich.domain.PayoutsTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayoutsRepoTest  extends CrudRepository<PayoutsTest, Long> {
    PayoutsTest save(PayoutsTest payouts);
}
