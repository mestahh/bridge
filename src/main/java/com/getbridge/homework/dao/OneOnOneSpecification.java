package com.getbridge.homework.dao;

import com.getbridge.homework.model.OneOnOne;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.Specification;

@EqualsAndHashCode(of = {"userId"})
public class OneOnOneSpecification implements Specification<OneOnOne> {

  public static final String PARTICIPANTS = "participants";
  public static final String USER_1_ID = "user1Id";
  public static final String USER_2_ID = "user2Id";
  private final long userId;

  public OneOnOneSpecification(long userId) {
    this.userId = userId;
  }

  @Override
  public Predicate toPredicate(Root<OneOnOne> root, CriteriaQuery<?> query,
      CriteriaBuilder criteriaBuilder) {
    Path<Object> user1Id = root.get(PARTICIPANTS).get(USER_1_ID);
    Path<Object> user2Id = root.get(PARTICIPANTS).get(USER_2_ID);
    return criteriaBuilder.or(criteriaBuilder.equal(user1Id, userId),
        criteriaBuilder.equal(user2Id, userId));
  }
}
