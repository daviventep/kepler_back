package com.kepler_apiweb.keplerapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kepler_apiweb.keplerapi.model.MainAdquisitionModel;
import com.kepler_apiweb.keplerapi.repository.IMainAdquisitionRepository;

@Service
public class MainAdquisitionServiceImp implements IMainAdquisitionService{
    
    @Autowired
    IMainAdquisitionRepository mainAdquisitionRepository;
    
    @Override
    public List<MainAdquisitionModel> listMainAdquisition(){
        return mainAdquisitionRepository.findAll();
    }

    @Override
    public MainAdquisitionModel createMainAdquisition(MainAdquisitionModel adquisition) {
        return mainAdquisitionRepository.save(adquisition);
    }

    public MainAdquisitionModel updateMainAdquisition(MainAdquisitionModel adquisition) {
        return mainAdquisitionRepository.save(adquisition);
    }
}
