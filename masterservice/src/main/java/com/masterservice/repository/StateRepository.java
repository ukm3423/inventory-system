package com.masterservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.masterservice.models.State;


public interface StateRepository extends JpaRepository<State, Long>{

  

}
