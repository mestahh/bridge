package com.getbridge.homework.dao;

import com.getbridge.homework.exceptions.NotAuthorizedException;
import com.getbridge.homework.model.OneOnOne;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OneOnOneDao {

  @Autowired
  private OneOnOneRepository repository;

  public OneOnOne save(OneOnOne oneOnOne) {
    return repository.save(oneOnOne);
  }

  public OneOnOne update(OneOnOne oneOnOne, Long userId) {
    Optional<OneOnOne> itemInDb = repository.findById(oneOnOne.getId());
    if (itemInDb.get().getParticipants().getUser1Id() != userId && itemInDb.get().getParticipants()
        .getUser2Id() != userId) {
      throw new NotAuthorizedException();
    }
    return repository.save(oneOnOne);

  }

  public void delete(long id, long userId) {
    Optional<OneOnOne> itemInDb = repository.findById(id);
    if (itemInDb.get().getParticipants().getUser1Id() != userId && itemInDb.get().getParticipants()
        .getUser2Id() != userId) {
      throw new NotAuthorizedException();
    }
    repository.deleteById(id);
  }
}
