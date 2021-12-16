package com.getbridge.homework.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getbridge.homework.dao.OneOnOneRepository;
import com.getbridge.homework.model.OneOnOne;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OneOnOneController.class)
public class OneOnOneControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper objectMapper = new ObjectMapper();

  @MockBean
  private OneOnOneRepository repository;

  @Test
  public void itShouldReturnAOneOnOneByItsId() throws Exception {
    OneOnOne result = new OneOnOne();
    result.setId(123L);
    when(repository.findById(123L)).thenReturn(Optional.of(result));

    mockMvc.perform(get("/one_on_ones/{id}", 123L)
            .contentType("application/json"))
        .andExpect(content().string(objectMapper.writeValueAsString(result)))
        .andExpect(status().isOk());
  }

  @Test
  public void itShouldReturnAnEmptyObjectIfNoneFound() throws Exception {
    when(repository.findById(123L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/one_on_ones/{id}", 123L)
            .contentType("application/json"))
        .andExpect(content().string(""))
        .andExpect(status().is(204));
  }

  @Test
  public void itShouldBeAbleToSaveOneOnOne() throws Exception {
    OneOnOne oneOnOne = new OneOnOne();
    oneOnOne.setTitle("title");

    OneOnOne oneOnOneWithId = new OneOnOne();
    oneOnOneWithId.setTitle("title");
    oneOnOneWithId.setId(1L);

    when(repository.save(oneOnOne)).thenReturn(oneOnOneWithId);

    mockMvc.perform(post("/one_on_ones").contentType("application/json")
            .content(objectMapper.writeValueAsString(oneOnOne)))
        .andExpect(status().is(201))
        .andExpect(content().string(objectMapper.writeValueAsString(oneOnOneWithId)));
    verify(repository).save(oneOnOne);
  }

  @Test
  public void savingAnObjectShouldReturnAnErrorIfObjectIsNull() throws Exception {
    mockMvc.perform(post("/one_on_ones").contentType("application/json")
            .content(""))
        .andExpect(status().is(400));
  }
}
