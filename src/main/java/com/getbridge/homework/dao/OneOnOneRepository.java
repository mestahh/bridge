package com.getbridge.homework.dao;

import com.getbridge.homework.model.OneOnOne;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OneOnOneRepository extends CrudRepository<OneOnOne, Long> {
  List<OneOnOne> findByClosed(Boolean closed);

  List<OneOnOne> findAll(Specification spec);

}
