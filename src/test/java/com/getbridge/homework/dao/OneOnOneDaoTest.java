package com.getbridge.homework.dao;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.getbridge.homework.exceptions.NotAuthorizedException;
import com.getbridge.homework.model.OneOnOne;
import com.getbridge.homework.model.Participants;
import java.util.List;
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
    OneOnOne oneOnOne = OneOnOne.builder().build();
    when(repository.save(oneOnOne)).thenReturn(oneOnOne);

    OneOnOne result = testObj.save(oneOnOne);

    verify(repository).save(oneOnOne);
  }

  @Test
  public void itShouldNotUpdateIfUserIsNotAuthorized() {
    OneOnOne oneOnOne = createOneOnOne(123L, 234L);
    OneOnOne oneOnOneInDb = createOneOnOne(456L, 789L);

    when(repository.findById(oneOnOne.getId())).thenReturn(Optional.of(oneOnOneInDb));

    assertThrows(NotAuthorizedException.class, () -> {
      testObj.update(oneOnOne, 123L);
    });
  }

  @Test
  public void itShouldUpdateIfUserIsAuthorized() {
    OneOnOne oneOnOne = createOneOnOne(123L, 234L);
    OneOnOne oneOnOneInDb = createOneOnOne(123L, 789L);

    when(repository.findById(oneOnOne.getId())).thenReturn(Optional.of(oneOnOneInDb));
    testObj.update(oneOnOne, 123L);

    verify(repository).save(oneOnOne);
  }

  @Test
  public void itShouldNotDeleteIfNotAuthorized() {
    OneOnOne oneOnOne = createOneOnOne(345L, 234L);

    when(repository.findById(123L)).thenReturn(Optional.of(oneOnOne));

    assertThrows(NotAuthorizedException.class, () -> testObj.delete(123L, 456L));
  }

  @Test
  public void itShouldBeAbleToDelete() {
    OneOnOne oneOnOne = createOneOnOne(123L, 234L);

    when(repository.findById(123L)).thenReturn(Optional.of(oneOnOne));

    testObj.delete(123L, 123L);
    verify(repository).deleteById(123L);
  }

  @Test
  public void itShouldThrowExceptionIfNotAuthorizedToFindById() {
    OneOnOne oneOnOne = createOneOnOne(123L, 234L);
    when(repository.findById(oneOnOne.getId())).thenReturn(Optional.of(oneOnOne));

    assertThrows(NotAuthorizedException.class, () -> testObj.findById(oneOnOne.getId(), 789L));
  }

  @Test
  public void itShouldReturnByAnId() {
    OneOnOne oneOnOne = createOneOnOne(123L, 234L);
    when(repository.findById(oneOnOne.getId())).thenReturn(Optional.of(oneOnOne));

    Optional<OneOnOne> result = testObj.findById(1L, 234L);
    assertTrue(result.isPresent());
  }

  @Test
  public void shouldReturnOnlyTheOnesWhereUserIdIsOneOfTheParticipants() {
    List<OneOnOne> result = testObj.findAll(789L);
    verify(repository).findAll(eq(new OneOnOneSpecification(789L)));
  }

  private OneOnOne createOneOnOne(long user1Id, long user2Id) {
    OneOnOne oneOnOne = OneOnOne.builder()
        .id(1)
        .participants(
            Participants.builder()
                .user1Id(user1Id)
                .user2Id(user2Id)
                .build()
        )
        .build();

    return oneOnOne;
  }
}


