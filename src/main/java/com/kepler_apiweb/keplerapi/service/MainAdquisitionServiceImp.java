package com.kepler_apiweb.keplerapi.service;

import java.util.List;
import java.util.Optional;

import com.kepler_apiweb.keplerapi.model.UserModel;
import com.kepler_apiweb.keplerapi.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kepler_apiweb.keplerapi.model.MainAdquisitionModel;
import com.kepler_apiweb.keplerapi.repository.IMainAdquisitionRepository;

@Service
public class MainAdquisitionServiceImp implements IMainAdquisitionService{
    
    @Autowired
    IMainAdquisitionRepository mainAdquisitionRepository;
    @Autowired
    IUserRepository userRepository;

    @Override
    public String saveMainAdquisition(MainAdquisitionModel adquisition) {
        Optional<UserModel>
                userOptional = userRepository.findById(adquisition.getUser_id().toString());
        if (userOptional.isPresent()) {
            mainAdquisitionRepository.save(adquisition);
            UserModel user = userOptional.get();
            return String.format("La adquisición del usuario %s %s ha sido exitosa.", user.getFirst_name(), user.getLast_name());
        } else {
            return String.format("¡Error! No se encontró el usuario con el Id %s", adquisition.getUser_id().toString());
        }
    }

    @Override
    public List<MainAdquisitionModel> listMainAdquisition(){
        return mainAdquisitionRepository.findAll();
    }

    public MainAdquisitionModel updateMainAdquisition(MainAdquisitionModel adquisition) {
        return mainAdquisitionRepository.save(adquisition);
    }
}
