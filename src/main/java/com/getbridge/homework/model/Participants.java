package com.getbridge.homework.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "participants")
@Data
@EqualsAndHashCode
public class Participants {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private Long user1Id;

  private Long user2Id;
}
