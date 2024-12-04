package com.smart.contacts.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.contacts.entity.User;

public interface UserRepository extends JpaRepository<User, String>
{
	Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByEmailToken(String id);
}
