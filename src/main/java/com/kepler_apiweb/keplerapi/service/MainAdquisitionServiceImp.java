package com.kepler_apiweb.keplerapi.service;

import java.util.List;
import java.util.Optional;

import com.kepler_apiweb.keplerapi.model.UserModel;
import com.kepler_apiweb.keplerapi.repository.IUserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kepler_apiweb.keplerapi.model.MainAdquisitionModel;
import com.kepler_apiweb.keplerapi.repository.IMainAdquisitionRepository;

@Service
public class MainAdquisitionServiceImp implements IMainAdquisitionService {
    @Autowired
    IMainAdquisitionRepository mainAdquisitionRepository;
    @Autowired
    IUserRepository userRepository;

    @Override
    public List<MainAdquisitionModel> validateAdquisitionsByUserStatus(String userId, String statusType) {
        ObjectId objectId = new ObjectId(userId);
        return mainAdquisitionRepository.findByUserIdStatus(objectId, statusType);
    }
    @Override
    public String saveMainAdquisition(MainAdquisitionModel adquisition) {
        Optional<UserModel>
                userOptional = userRepository.findById(adquisition.getUser_id().toString());
        if (userOptional.isPresent()) {
            mainAdquisitionRepository.save(adquisition);
            UserModel user = userOptional.get();
            int quantityDetails = adquisition.getAdquisition_details().size();
            String quantityProducts;
            if (quantityDetails > 1) {
                quantityProducts = "ha almacenado los productos";
            } else {
                quantityProducts = "ha almacenado el producto";
            }
            return String.format("Se %s al usuario %s %s.",
                    quantityProducts, user.getFirst_name(), user.getLast_name());
        } else {
            return String.format("¡Error! No se encontró el usuario con el Id %s", adquisition.getUser_id().toString());
        }
    }
    @Override
    public String addAdquisition(MainAdquisitionModel adquisition) {
        ObjectId objectId = new ObjectId(adquisition.getUser_id().toString());
        return String.format("Holaaa");
    }

    @Override
    public List<MainAdquisitionModel> listMainAdquisition(){
        return mainAdquisitionRepository.findAll();
    }
    // Filtrar un Main Adquisition por Id
    @Override
    public Optional<MainAdquisitionModel> getMainAdquisitionById(String mainAdquisitionId) {
        return mainAdquisitionRepository.findById(mainAdquisitionId);
    }
    // Filtrar Main Adquisition por User
    @Override
    public List<MainAdquisitionModel> getMainAdquisitionsByUser(String userId) {
        ObjectId objectId = new ObjectId(userId);
        return mainAdquisitionRepository.findByUserId(objectId);
    }

    public MainAdquisitionModel updateMainAdquisition(MainAdquisitionModel adquisition) {
        return mainAdquisitionRepository.save(adquisition);
    }

}
