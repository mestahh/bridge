package com.getbridge.homework.dao;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.getbridge.homework.exceptions.NotAuthorizedException;
import com.getbridge.homework.model.OneOnOne;
import com.getbridge.homework.model.Participants;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OneOnOneDaoTest {

  @Autowired
  private OneOnOneDao testObj;

  @MockBean
  private OneOnOneRepository repository;

  @Test
  public void itShouldBeAbleToSave() {
    OneOnOne oneOnOne = new OneOnOne();
    when(repository.save(oneOnOne)).thenReturn(oneOnOne);

    OneOnOne result = testObj.save(oneOnOne);

    verify(repository).save(oneOnOne);
  }

  @Test
  public void itShouldNotUpdateIfUserIsNotAuthorized() {
    OneOnOne oneOnOne = new OneOnOne();
    oneOnOne.setId(1);
    Participants participants1 = new Participants();
    participants1.setUser1Id(123L);
    participants1.setUser2Id(234L);
    oneOnOne.setParticipants(participants1);

    OneOnOne oneOnOneInDb = new OneOnOne();
    Participants participants2 = new Participants();
    participants2.setUser1Id(456L);
    participants2.setUser2Id(789L);
    oneOnOneInDb.setParticipants(participants2);

    when(repository.findById(oneOnOne.getId())).thenReturn(Optional.of(oneOnOneInDb));

    assertThrows(NotAuthorizedException.class, () -> {
      testObj.update(oneOnOne, 123L);
    });
  }

  @Test
  public void itShouldUpdateIfUserIsAuthorized() {
    OneOnOne oneOnOne = new OneOnOne();
    oneOnOne.setId(1);
    Participants participants1 = new Participants();
    participants1.setUser1Id(123L);
    participants1.setUser2Id(234L);
    oneOnOne.setParticipants(participants1);

    OneOnOne oneOnOneInDb = new OneOnOne();
    Participants participants2 = new Participants();
    participants2.setUser1Id(123L);
    participants2.setUser2Id(789L);
    oneOnOneInDb.setParticipants(participants2);

    when(repository.findById(oneOnOne.getId())).thenReturn(Optional.of(oneOnOneInDb));
    testObj.update(oneOnOne, 123L);

    verify(repository).save(oneOnOne);
  }

  @Test
  public void itShouldNotDeleteIfNotAuthorized() {
    OneOnOne oneOnOne = new OneOnOne();
    oneOnOne.setId(1);
    Participants participants1 = new Participants();
    participants1.setUser1Id(345L);
    participants1.setUser2Id(234L);
    oneOnOne.setParticipants(participants1);

    when(repository.findById(123L)).thenReturn(Optional.of(oneOnOne));

    assertThrows(NotAuthorizedException.class, () -> {
      testObj.delete(123L, 456L);
    });
  }

  @Test
  public void itShouldBeAbleToDelete() {
    OneOnOne oneOnOne = new OneOnOne();
    oneOnOne.setId(1);
    Participants participants1 = new Participants();
    participants1.setUser1Id(123L);
    participants1.setUser2Id(234L);
    oneOnOne.setParticipants(participants1);

    when(repository.findById(123L)).thenReturn(Optional.of(oneOnOne));

    testObj.delete(123L, 123L);
    verify(repository).deleteById(123L);
  }
}


