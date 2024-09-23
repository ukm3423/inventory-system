package com.masterservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masterservice.models.City;
import com.masterservice.models.State;


public interface CityRepository extends JpaRepository<City, Long> {
    
    List<City> findByState(State state);

}
