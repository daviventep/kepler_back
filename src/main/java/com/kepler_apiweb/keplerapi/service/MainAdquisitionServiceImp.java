package com.kepler_apiweb.keplerapi.service;

import java.util.List;
import java.util.Optional;

import com.kepler_apiweb.keplerapi.DTO.MainAdquisitionPurchaseDTO;
import com.kepler_apiweb.keplerapi.model.UserModel;
import com.kepler_apiweb.keplerapi.repository.IUserRepository;
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
    public List<MainAdquisitionModel> validateAdquisitionsByUserStatus(int userId, String statusType) {
        return mainAdquisitionRepository.findByUserIdStatus(userId, statusType);
    }
    @Override
    public String saveMainAdquisition(MainAdquisitionModel adquisition) {
        Optional<UserModel>
                userOptional = userRepository.findById(adquisition.getUser_id());
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
            return String.format("¡Error! No se encontró el usuario con el Id %s", adquisition.getUser_id());
        }
    }

    @Override
    public String makePurchase(MainAdquisitionPurchaseDTO makeAdquisition) {
        String return_process;
        return_process = "Recibido";
        return return_process;
    }

    @Override
    public List<MainAdquisitionModel> listMainAdquisition(){
        return mainAdquisitionRepository.findAll();
    }
    // Filtrar un Main Adquisition por Id
    @Override
    public Optional<MainAdquisitionModel> getMainAdquisitionById(int mainAdquisitionId) {
        return mainAdquisitionRepository.findById(mainAdquisitionId);
    }
    // Filtrar Main Adquisition por User
    @Override
    public List<MainAdquisitionModel> getMainAdquisitionsByUser(int userId) {
        return mainAdquisitionRepository.findByUserId(userId);
    }
    @Override
    public int getNextId() {
        int return_num;
        List<MainAdquisitionModel> listAdquisitions = mainAdquisitionRepository.findLastMainAdquisition();
        System.out.println(listAdquisitions);
        if (!listAdquisitions.isEmpty() && listAdquisitions.get(0) != null) {
            return_num = listAdquisitions.get(0).get_id() + 1;
        } else {
            return_num = 1;
        }
        return return_num;
    }

    public MainAdquisitionModel updateMainAdquisition(MainAdquisitionModel adquisition) {
        return mainAdquisitionRepository.save(adquisition);
    }

}
