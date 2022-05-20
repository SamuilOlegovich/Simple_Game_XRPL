package com.samuilolegovich.repository;

import com.samuilolegovich.domain.UserTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepoTest extends CrudRepository<UserTest, Long> {
    Optional<UserTest> findById(Long id);
    Optional<UserTest> findByUuid(String string);
    Optional<UserTest> findByIdAndUuid(Long id, String string);
}
