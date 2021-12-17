package com.getbridge.homework.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getbridge.homework.dao.OneOnOneDao;
import com.getbridge.homework.dao.OneOnOneRepository;
import com.getbridge.homework.model.OneOnOne;
import java.util.ArrayList;
import java.util.List;
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

  @MockBean
  private OneOnOneDao dao;

  @Test
  public void itShouldReturnAllOneOnOnes() throws Exception {
    OneOnOne oneOnOne = new OneOnOne();
    oneOnOne.setId(123L);
    List<OneOnOne> oneOnOnes = new ArrayList<>();
    oneOnOnes.add(oneOnOne);

    when(dao.findAll(789L)).thenReturn(oneOnOnes);

    mockMvc.perform(get("/one_on_ones")
            .header("X-AUTHENTICATED-USER", "789")
            .contentType("application/json"))
        .andExpect(content().string(objectMapper.writeValueAsString(oneOnOnes)))
        .andExpect(status().isOk());
  }

  @Test
  public void itShouldReturnAOneOnOneByItsId() throws Exception {
    OneOnOne result = new OneOnOne();
    result.setId(123L);
    when(dao.findById(123L, 789L)).thenReturn(Optional.of(result));

    mockMvc.perform(get("/one_on_ones/{id}", 123L)
            .header("X-AUTHENTICATED-USER", "789")
            .contentType("application/json"))
        .andExpect(content().string(objectMapper.writeValueAsString(result)))
        .andExpect(status().isOk());
  }

  @Test
  public void itShouldReturnAnEmptyObjectIfNoneFound() throws Exception {
    when(dao.findById(123L, 789L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/one_on_ones/{id}", 123L)
            .header("X-AUTHENTICATED-USER", "789")
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

    when(dao.save(oneOnOne)).thenReturn(oneOnOneWithId);

    mockMvc.perform(post("/one_on_ones").contentType("application/json")
            .content(objectMapper.writeValueAsString(oneOnOne)))
        .andExpect(status().is(201))
        .andExpect(content().string(objectMapper.writeValueAsString(oneOnOneWithId)));

    verify(dao).save(oneOnOne);
  }

  @Test
  public void savingAnObjectShouldReturnAnErrorIfObjectIsNull() throws Exception {
    mockMvc.perform(post("/one_on_ones").contentType("application/json")
            .content(""))
        .andExpect(status().is(400));
  }

  @Test
  public void itShouldBeAbleToDeleteAOneOnOne() throws Exception {
    mockMvc.perform(delete("/one_on_ones/{id}", 123L)
            .header("X-AUTHENTICATED-USER", "789")
            .contentType("application/json"))
        .andExpect(status().is(202));
    verify(dao).delete(123L, 789L);
  }

  @Test
  public void itShouldBeAbleToCloseOneOnOnes() throws Exception {
    OneOnOne oneOnOne = new OneOnOne();
    oneOnOne.setId(123L);

    OneOnOne updatedOneOnOne = new OneOnOne();
    updatedOneOnOne.setId(123L);
    updatedOneOnOne.setClosed(true);

    when(dao.update(updatedOneOnOne, 789L)).thenReturn(updatedOneOnOne);
    when(dao.findById(123L, 789L)).thenReturn(Optional.of(oneOnOne));

    mockMvc.perform(post("/one_on_ones/{id}/close", 123L).contentType("application/json")
            .header("X-AUTHENTICATED-USER", "789")
            .content(objectMapper.writeValueAsString(oneOnOne)))
        .andExpect(status().is(201))
        .andExpect(content().string(objectMapper.writeValueAsString(updatedOneOnOne)));

    verify(dao).update(updatedOneOnOne, 789L);
  }

  @Test
  public void itShouldReturnAnErrorOnClosingNotExistingOneOnOne() throws Exception {
    OneOnOne oneOnOne = new OneOnOne();
    oneOnOne.setId(123L);

    mockMvc.perform(post("/one_on_ones/{id}/close", 123L)
            .contentType("application/json")
            .header("X-AUTHENTICATED-USER", "789")
            .content(objectMapper.writeValueAsString(oneOnOne)))
        .andExpect(status().is(400));

    verify(dao).findById(123L, 789L);
    verifyNoMoreInteractions(repository);
  }

  @Test
  public void itShouldBeAbleToUpdateAnItem() throws Exception {
    OneOnOne updatedOneOnOne = new OneOnOne();
    updatedOneOnOne.setId(123L);
    updatedOneOnOne.setDescription("desc");
    updatedOneOnOne.setTitle("title");

    when(dao.update(updatedOneOnOne, 789L)).thenReturn(updatedOneOnOne);

    mockMvc.perform(put("/one_on_ones").contentType("application/json")
            .header("X-AUTHENTICATED-USER", "789")
            .content(objectMapper.writeValueAsString(updatedOneOnOne)))
        .andExpect(status().is(201));

    verify(dao).update(updatedOneOnOne, 789L);
  }

  @Test
  public void itReturnsAnErrorWhenThereIsNoUpdateObject() throws Exception {
    mockMvc.perform(put("/one_on_ones").contentType("application/json")
        .content("")).andExpect(status().is(400));

    verifyNoMoreInteractions(repository);
  }

  @Test
  public void itShouldFilterClosedOneOnOnes() throws Exception {
    OneOnOne oneOnOne = new OneOnOne();
    oneOnOne.setId(123L);
    List<OneOnOne> oneOnOnes = new ArrayList<>();
    oneOnOnes.add(oneOnOne);

    when(repository.findByClosed(true)).thenReturn(oneOnOnes);

    mockMvc.perform(get("/one_on_ones?closed={closed}", true)
            .header("X-AUTHENTICATED-USER", "789")
            .contentType("application/json"))
        .andExpect(content().string(objectMapper.writeValueAsString(oneOnOnes)))
        .andExpect(status().isOk());
  }

  @Test
  public void itShouldFilterNotClosedOneOnOnes() throws Exception {
    OneOnOne oneOnOne = new OneOnOne();
    oneOnOne.setId(123L);
    List<OneOnOne> oneOnOnes = new ArrayList<>();
    oneOnOnes.add(oneOnOne);

    when(repository.findByClosed(false)).thenReturn(oneOnOnes);

    mockMvc.perform(get("/one_on_ones?closed={closed}", false)
            .header("X-AUTHENTICATED-USER", "789")
            .contentType("application/json"))
        .andExpect(content().string(objectMapper.writeValueAsString(oneOnOnes)))
        .andExpect(status().isOk());
  }
}
