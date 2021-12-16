package com.getbridge.homework.controllers;

import com.getbridge.homework.dao.OneOnOneRepository;
import com.getbridge.homework.model.OneOnOne;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class OneOnOneController {

  @Autowired
  private OneOnOneRepository repository;

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
  public OneOnOne save(@RequestBody OneOnOne oneOnOne) {
    if (oneOnOne == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Please provide a proper OneOnOne object in the request body");
    }
    return repository.save(oneOnOne);
  }
}