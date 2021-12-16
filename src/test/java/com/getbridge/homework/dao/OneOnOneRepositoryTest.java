package com.getbridge.homework.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.getbridge.homework.model.OneOnOne;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@DataJpaTest
public class OneOnOneRepositoryTest {
  
    @Autowired
    private OneOnOneRepository oneOnOneRepository;
   
    @Test
    public void itShouldSaveOneOnOnes() {
        OneOnOne oneOnOne = new OneOnOne();
        
        oneOnOneRepository.save(oneOnOne);
        List<OneOnOne> oneOnOnes = (List<OneOnOne>) oneOnOneRepository.findAll();
        
        assertEquals(oneOnOnes.size(), 1);
    }    
}
