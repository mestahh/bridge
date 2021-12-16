package com.getbridge.homework.controllers;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class OneOnOneController {

  @Autowired
  private OneOnOneRepository repository;

  @GetMapping("/one_on_ones")
  public List<OneOnOne> getOneOnOnes() {
    Iterable<OneOnOne> result = repository.findAll();
    return StreamSupport.stream(result.spliterator(), false)
        .collect(Collectors.toList());
  }

  @GetMapping("/one_on_ones/{id}")
  public OneOnOne getOneOnOne(@PathVariable("id") Long id) {
    Optional<OneOnOne> oneOnOne = repository.findById(id);
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
    OneOnOne saved = repository.save(oneOnOne);
    return new ResponseEntity<>(saved, HttpStatus.CREATED);
  }

  @DeleteMapping("/one_on_ones/{id}")
  public ResponseEntity delete(@PathVariable("id") Long id) {
    repository.deleteById(id);
    return new ResponseEntity(HttpStatus.ACCEPTED);
  }

  @PostMapping("/one_on_ones/{id}/close")
  public ResponseEntity<OneOnOne> close(@PathVariable("id") Long id) {
    Optional<OneOnOne> saved = repository.findById(id);
    if (saved.isPresent()) {
      OneOnOne savedOneOnOne = saved.get();
      savedOneOnOne.setClosed(true);
      repository.save(savedOneOnOne);
      return new ResponseEntity<>(savedOneOnOne, HttpStatus.CREATED);
    }
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
        "There is no One on one in the db with the id : " + id);
  }

  @PutMapping("/one_on_ones")
  public ResponseEntity<OneOnOne> update(@RequestBody OneOnOne oneOnOne) {
    repository.save(oneOnOne);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}