package com.samuilolegovich.repository;


import com.samuilolegovich.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepo extends CrudRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByUuid(String string);
    Optional<User> findByIdAndUuid(Long id, String string);
    User save(User user);

}
