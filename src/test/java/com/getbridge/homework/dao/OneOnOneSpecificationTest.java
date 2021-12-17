package com.getbridge.homework.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.getbridge.homework.model.OneOnOne;
import com.getbridge.homework.model.Participants;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class OneOnOneSpecificationTest {

  @Autowired
  private OneOnOneRepository testObj;

  @BeforeEach
  public void setUp() {
    saveOneOnOne(345L, 456L);
    saveOneOnOne(123L, 234L);
    saveOneOnOne(234L, 123L);
    saveOneOnOne(456L, 567L);
  }

  private void saveOneOnOne(Long user1Id, Long user2Id) {
    OneOnOne oneOnOne = new OneOnOne();
    Participants participants = new Participants();
    participants.setUser1Id(user1Id);
    participants.setUser2Id(user2Id);
    oneOnOne.setParticipants(participants);
    testObj.save(oneOnOne);
  }

  @Test
  public void itReturnsNothingIfUserIdIsNotAmongParticipants() {
    Specification<OneOnOne> spec = OneOnOneSpecification.isAuthorized(789L);
    List<OneOnOne> result = testObj.findAll(spec);
    assertEquals(0, result.size());
  }

  @Test
  public void itReturnsAllWhereUserIdIsAmongParticipants() {
    Specification<OneOnOne> spec = OneOnOneSpecification.isAuthorized(123L);
    List<OneOnOne> result = testObj.findAll(spec);
    assertEquals(2, result.size());
    assertEquals(123L, result.get(0).getParticipants().getUser1Id());
    assertEquals(234L, result.get(0).getParticipants().getUser2Id());

    assertEquals(234L, result.get(1).getParticipants().getUser1Id());
    assertEquals(123L, result.get(1).getParticipants().getUser2Id());
  }

  @Test
  public void itReturnsAllWhereUserIdIs567L() {
    Specification<OneOnOne> spec = OneOnOneSpecification.isAuthorized(567L);
    List<OneOnOne> result = testObj.findAll(spec);
    assertEquals(1, result.size());
    assertEquals(456L, result.get(0).getParticipants().getUser1Id());
    assertEquals(567L, result.get(0).getParticipants().getUser2Id());
  }

  @Test
  public void itReturnsAllWhereUserIdIs345L() {
    Specification<OneOnOne> spec = OneOnOneSpecification.isAuthorized(345L);
    List<OneOnOne> result = testObj.findAll(spec);
    assertEquals(1, result.size());
    assertEquals(345L, result.get(0).getParticipants().getUser1Id());
    assertEquals(456L, result.get(0).getParticipants().getUser2Id());
  }
}
