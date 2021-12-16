package com.getbridge.homework.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "one_on_ones")
public class OneOnOne {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String title;
  private String participants;
  private LocalDate plannedTime;
  private String description;
  private String location;

  public void setId(Long id) {
    this.id = id;
  }
}
