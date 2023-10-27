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
    public String saveMainAdquisition(MainAdquisitionModel adquisition, Boolean creation, Boolean updating) {
        Optional<UserModel>
                userOptional = userRepository.findById(adquisition.getUser_id());
        if (userOptional.isPresent()) {
            mainAdquisitionRepository.save(adquisition);
            UserModel user = userOptional.get();
            String textReturn = "han realizado las acciones respectivas";
            if (creation == true) {
                textReturn = "ha creado la adquisición";
            } else if (updating) {
                textReturn = "ha actualizado la adquisición";
            }
            return String.format("Se %s al usuario %s %s.",
                    textReturn, user.getFirst_name(), user.getLast_name());
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
    public String getMainAdquisitionByIdAndUserId(int id, int user_id) {
        Boolean existIdAndUserIdAndStatus = mainAdquisitionRepository.findBy_idAndUser_idAndStatus(id, user_id,
                "Pendiente").isPresent();
        String return_value;
        if (existIdAndUserIdAndStatus == true) {
            return_value = "";
        } else {
            Optional<MainAdquisitionModel> existUser_idAndStatus =
                    mainAdquisitionRepository.findByUser_idAndStatus(user_id, "Pendiente");
            if (existUser_idAndStatus.isPresent()) {
                return_value = String.format("¡Error! El usuario con iD %d ya tiene una adquisición pendiente con un " +
                                "iD distinto a %d.",
                        user_id, id);
            } else {
                if (mainAdquisitionRepository.findById(id).isPresent() == true) {
                    int nextIdInt = getNextId();
                    return_value = String.format("La adquisición con iD %d ya existe, puedes usar el iD %d.",
                            id,
                            nextIdInt);
                } else {
                    return_value = "";
                }
            }
        }
        return return_value;
    }
    @Override
    public int getNextId() {
        int return_num;
        List<MainAdquisitionModel> listAdquisitions = mainAdquisitionRepository.findLastMainAdquisition();
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
