package com.getbridge.homework.dao;

import com.getbridge.homework.model.OneOnOne;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OneOnOneRepository extends CrudRepository<OneOnOne, Long> {}
