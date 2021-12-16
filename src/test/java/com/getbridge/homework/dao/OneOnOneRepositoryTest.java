package com.getbridge.homework.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.getbridge.homework.model.OneOnOne;
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
    OneOnOne oneOnOne = new OneOnOne();

    testObj.save(oneOnOne);
    List<OneOnOne> oneOnOnes = (List<OneOnOne>) testObj.findAll();

    assertEquals(oneOnOnes.size(), 1);
  }
  
  @Test
  public void itUpdatesAOneOnOne() {
    OneOnOne oneOnOne = new OneOnOne();

    OneOnOne updated = testObj.save(oneOnOne);
    updated.setTitle("new title");
    testObj.save(updated);

    Optional<OneOnOne> updatedFromDb = testObj.findById(updated.getId());
    assertEquals(updatedFromDb.get().getTitle(), "new title");
  }

  @Test
  public void itShouldDeleteOneOnOnes() {
    OneOnOne oneOnOne = new OneOnOne();

    testObj.save(oneOnOne);
    List<OneOnOne> oneOnOnes = (List<OneOnOne>) testObj.findAll();

    assertEquals(oneOnOnes.size(), 1);

    testObj.delete(oneOnOne);
    oneOnOnes = (List<OneOnOne>) testObj.findAll();
    assertEquals(oneOnOnes.size(), 0);
  }
}
