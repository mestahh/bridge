package com.getbridge.homework.controllers;

import com.getbridge.homework.dao.OneOnOneDao;
import com.getbridge.homework.dao.OneOnOneRepository;
import com.getbridge.homework.model.OneOnOne;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class OneOnOneController {

  @Autowired
  private OneOnOneRepository repository;
  @Autowired
  private OneOnOneDao dao;

  @GetMapping("/one_on_ones")
  public List<OneOnOne> getOneOnOnes(@RequestParam Optional<Boolean> closed,
      @RequestHeader("X-AUTHENTICATED-USER") Long userId) {
    Iterable<OneOnOne> result = dao.findAll(userId);
    List<OneOnOne> resultList = StreamSupport.stream(result.spliterator(), false)
        .collect(Collectors.toList());
    if (closed.isPresent()) {
      return resultList.stream().filter(i -> i.isClosed() == closed.get()).collect(
          Collectors.toList());
    }
    return resultList;
  }

  @GetMapping("/one_on_ones/{id}")
  public OneOnOne getOneOnOne(@PathVariable("id") Long id,
      @RequestHeader("X-AUTHENTICATED-USER") Long userId) {
    Optional<OneOnOne> oneOnOne = dao.findById(id, userId);
    if (!oneOnOne.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NO_CONTENT,
          "There was no one on one with the id + " + id);
    }
    return oneOnOne.get();
  }

  @PostMapping("/one_on_ones")
  public ResponseEntity<OneOnOne> save(@RequestBody OneOnOne oneOnOne) {
    if (oneOnOne == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Please provide a proper OneOnOne object in the request body");
    }
    OneOnOne saved = dao.save(oneOnOne);
    return new ResponseEntity<>(saved, HttpStatus.CREATED);
  }

  @DeleteMapping("/one_on_ones/{id}")
  public ResponseEntity delete(@PathVariable("id") Long id,
      @RequestHeader("X-AUTHENTICATED-USER") Long userId) {
    dao.delete(id, userId);
    return new ResponseEntity(HttpStatus.ACCEPTED);
  }

  @PostMapping("/one_on_ones/{id}/close")
  public ResponseEntity<OneOnOne> close(@PathVariable("id") Long id,
      @RequestHeader("X-AUTHENTICATED-USER") Long userId) {
    Optional<OneOnOne> saved = dao.findById(id, userId);
    if (saved.isPresent()) {
      OneOnOne savedOneOnOne = saved.get();
      savedOneOnOne.setClosed(true);
      dao.update(savedOneOnOne, userId);
      return new ResponseEntity<>(savedOneOnOne, HttpStatus.CREATED);
    }
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
        "There is no One on one in the db with the id : " + id);
  }

  @PutMapping("/one_on_ones")
  public ResponseEntity<OneOnOne> update(@RequestBody OneOnOne oneOnOne,
      @RequestHeader("X-AUTHENTICATED-USER") Long userId) {
    if (oneOnOne == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Please provide a proper OneOnOne object in the request body");
    }
    OneOnOne saved = dao.update(oneOnOne, userId);
    return new ResponseEntity<>(saved, HttpStatus.CREATED);
  }

}