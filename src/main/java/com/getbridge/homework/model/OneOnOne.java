package com.getbridge.homework.model;

import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "one_on_ones")
@Data
@EqualsAndHashCode
public class OneOnOne {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String title;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "participants_id", referencedColumnName = "id")
  private Participants participants;
  private LocalDate plannedTime;
  private String description;
  private String location;

  private boolean closed;
}
