package com.getbridge.homework;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.getbridge.homework.controllers.OneOnOneController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HomeworkApplicationTests {

  @Autowired
  private OneOnOneController controller;

  @Test
  void contextLoads() {
    assertNotNull(controller);
  }

}
