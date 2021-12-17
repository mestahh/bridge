package com.getbridge.homework.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.getbridge.homework.model.OneOnOne;
import com.getbridge.homework.model.Participants;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class OneOnOneRepositoryTest {

  @Autowired
  private OneOnOneRepository testObj;

  @Test
  public void itShouldSaveOneOnOnes() {
    OneOnOne oneOnOne = OneOnOne.builder().build();

    testObj.save(oneOnOne);
    List<OneOnOne> oneOnOnes = (List<OneOnOne>) testObj.findAll();

    assertEquals(oneOnOnes.size(), 1);
  }

  @Test
  public void itUpdatesAOneOnOne() {
    OneOnOne oneOnOne = OneOnOne.builder().build();

    OneOnOne updated = testObj.save(oneOnOne);
    updated.setTitle("new title");
    testObj.save(updated);

    Optional<OneOnOne> updatedFromDb = testObj.findById(updated.getId());
    assertEquals(updatedFromDb.get().getTitle(), "new title");
  }

  @Test
  public void itShouldDeleteOneOnOnes() {
    OneOnOne oneOnOne = OneOnOne.builder().build();

    testObj.save(oneOnOne);
    List<OneOnOne> oneOnOnes = (List<OneOnOne>) testObj.findAll();

    assertEquals(oneOnOnes.size(), 1);

    testObj.delete(oneOnOne);
    oneOnOnes = (List<OneOnOne>) testObj.findAll();
    assertEquals(oneOnOnes.size(), 0);
  }

  @Test
  public void itShouldFilterOneOnOnes() {
    OneOnOne item1 = OneOnOne.builder()
        .id(123L)
        .closed(true)
        .build();

    OneOnOne item2 = OneOnOne.builder()
        .id(234L)
        .closed(false)
        .build();

    testObj.save(item1);
    testObj.save(item2);

    List<OneOnOne> oneOnOnes = testObj.findByClosed(true);
    assertEquals(oneOnOnes.size(), 1);
    assertTrue(oneOnOnes.get(0).isClosed());
  }

  @Test
  public void itShouldSaveParticipants() {
    OneOnOne oneOnOne = OneOnOne.builder()
        .participants(
            Participants.builder()
                .user1Id(123L)
                .user2Id(234L)
                .build()
        )
        .build();

    testObj.save(oneOnOne);
    List<OneOnOne> oneOnOnes = (List<OneOnOne>) testObj.findAll();

    assertEquals(oneOnOnes.size(), 1);
    assertEquals(oneOnOnes.get(0).getParticipants().getUser1Id(), 123L);
    assertEquals(oneOnOnes.get(0).getParticipants().getUser2Id(), 234L);
  }
}
