package com.masterservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masterservice.models.City;
import com.masterservice.models.State;
import com.masterservice.services.CityService;



/**
 * * ===========================================================================
 * * ======================== Module : City-Controller =======================
 * * ======================== Created By : Umesh Kumar =========================
 * * ======================== Created On : 04-06-2024 ==========================
 * * ===========================================================================
 * * | Code Status : On
 */

@RestController
@CrossOrigin
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityService cityService;

    /**
     * * Getting All Cities BY State ID
     * * API : http://localhost:8080/api/city/2
     * @param stateId
     * @return
     */
    @GetMapping("/{id}")
    public List<City> findCityByStateId(@PathVariable("id") Long stateId) {
        return cityService.findCityByStateId(stateId);
    }

    /**
     * * Get All States 
     * * API : http://localhost:8080/api/city/get-list
     * @return
     */
    @GetMapping("/get-list")
    public List<State> getAllStates() {
        return cityService.getStateList();
    }

}
