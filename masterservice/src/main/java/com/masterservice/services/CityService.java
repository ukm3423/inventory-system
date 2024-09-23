package com.masterservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masterservice.models.City;
import com.masterservice.models.State;
import com.masterservice.repository.CityRepository;
import com.masterservice.repository.StateRepository;


@Service
public class CityService {
    
    @Autowired
    private CityRepository cityRepo;

    @Autowired
    private StateRepository stateRepo;


    public List<City> findCityByStateId(Long stateId){
        
        State state = stateRepo.findById(stateId).get(); 
        
        return cityRepo.findByState(state);

    }

    public List<State> getStateList() {
        return stateRepo.findAll();
    }

    public City getCityById(Long cityId) {
        return cityRepo.findById(cityId).get();
    }

    public State getStateById(Long stateId) {
        return stateRepo.findById(stateId).get();
    }

}
