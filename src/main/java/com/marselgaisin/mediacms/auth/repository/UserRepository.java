package com.marselgaisin.mediacms.auth.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marselgaisin.mediacms.auth.model.AppUser;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
}
