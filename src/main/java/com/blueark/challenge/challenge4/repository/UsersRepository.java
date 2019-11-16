package com.blueark.challenge.challenge4.repository;

import com.blueark.challenge.challenge4.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UserData, String> {
}
