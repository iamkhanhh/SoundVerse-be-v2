package com.TLU.SoundVerse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TLU.SoundVerse.entity.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
  Contract findByUserId(Integer userId);
}