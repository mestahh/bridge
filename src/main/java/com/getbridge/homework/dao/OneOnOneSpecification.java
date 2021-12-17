package com.getbridge.homework.dao;

import com.getbridge.homework.model.OneOnOne;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class OneOnOneSpecification {

  public static Specification<OneOnOne> isAuthorized(long userId) {
    Specification<OneOnOne> user1Spec = new Specification<OneOnOne>() {
      @Override
      public Predicate toPredicate(Root<OneOnOne> root, CriteriaQuery<?> query,
          CriteriaBuilder criteriaBuilder) {
        Path<Object> id = root.get("participants").get("user1Id");
        return criteriaBuilder.equal(id, userId);
      }
    };

    Specification<OneOnOne> user2Spec = new Specification<OneOnOne>() {
      @Override
      public Predicate toPredicate(Root<OneOnOne> root, CriteriaQuery<?> query,
          CriteriaBuilder criteriaBuilder) {
        Path<Object> id = root.get("participants").get("user2Id");
        return criteriaBuilder.equal(id, userId);
      }
    };

    return user1Spec.or(user2Spec);
  }
}
