package com.kepler_apiweb.keplerapi.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.kepler_apiweb.keplerapi.DTO.MainAdquisitionCompleteDTO;
import com.kepler_apiweb.keplerapi.DTO.MainAdquisitionPurchaseDTO;
import com.kepler_apiweb.keplerapi.DTO.ProductWithCategoryDTO;
import com.kepler_apiweb.keplerapi.model.UserModel;
import com.kepler_apiweb.keplerapi.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.kepler_apiweb.keplerapi.model.MainAdquisitionModel;
import com.kepler_apiweb.keplerapi.repository.IMainAdquisitionRepository;

@Service
public class MainAdquisitionServiceImp implements IMainAdquisitionService {
    @Autowired
    IMainAdquisitionRepository mainAdquisitionRepository;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    private MongoTemplate
            mongoTemplate;

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


    public List<MainAdquisitionCompleteDTO> listMainAdquisionTemplate(List<Criteria> matchCriterias) {
        Criteria[] criteriaArray = matchCriterias.toArray(new Criteria[0]);
        Aggregation
                aggregation;

        AggregationOperation
                lookupOperationProduct = Aggregation.lookup("Product", "adquisition_details.product_id", "_id",
                "productData");
        AggregationOperation unwindOperationProduct = Aggregation.unwind("$productData");

        AggregationOperation
                lookupOperationUser = Aggregation.lookup("User", "user_id", "_id",
                "userData");
        AggregationOperation unwindOperationUser = Aggregation.unwind("$userData");

        AggregationOperation projectOperation = Aggregation.project()
//                .andExclude("_id")
                .and("money_total_value").as("money_total_value")
                .and("point_total_value").as("point_total_value")
                .and("point_unit_price").as("point_unit_price")
                .and("creation_date").as("creation_date")
                .and("delivery_date").as("delivery_date")
                .and("user_id").as("user_id")
                .and("measure_unit").as("measure_unit")
                .and("status").as("status")
                .and("adquisition_details").as("adquisition_details")
//                .and("productData.name").as("productData.name")
                .and("userData.first_name").as("userData.first_name")
                .and("userData.last_name").as("userData.last_name")
                .and("userData.identification").as("userData.identification");
//                .and("category_id").as("category_id");
        if (criteriaArray.length > 0) {
            aggregation = Aggregation.newAggregation(
                    lookupOperationProduct, unwindOperationProduct,
                    lookupOperationUser, unwindOperationUser,
                    Aggregation.match(new Criteria().andOperator(criteriaArray)), projectOperation);
        } else {
            aggregation = Aggregation.newAggregation(
                    lookupOperationProduct, unwindOperationProduct,
                    lookupOperationUser, unwindOperationUser,
                    projectOperation);
        }
        AggregationResults<MainAdquisitionCompleteDTO>
                results = mongoTemplate.aggregate(aggregation, "Main_Adquisition", MainAdquisitionCompleteDTO.class);
        List<MainAdquisitionCompleteDTO> resultList = results.getMappedResults();

        return resultList;
    }

    // SOLUCIÓN A PREGUNTA 1
    public List<MainAdquisitionCompleteDTO> findSalesByUserAndOrderByDate(int userId) {
        return mainAdquisitionRepository.findSalesByUserIdOrderByDate(userId, "Realizada");
    }
    // SOLUCIÓN A PREGUNTA 2
    @Override
    public List<MainAdquisitionCompleteDTO> listMainAdquisitionDay(String date) {
        date = date.replace("-", "/");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            // Convertimos la fecha recibida en tipo Date
            // formato de String: dd-MM-yyyy
            Date targetDate = dateFormat.parse(date);
            // Le sumamos restamos 5 horas a la fecha que enviamos
            Date startDate = new Date(targetDate.getTime() - 18000000);
            // Le sumamos sumamos 5 horas a la fecha que enviamos
            Date endDate = new Date(targetDate.getTime() + 68400000);

            // Creamos un formato para volver las nuevas fechas Strings
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            // Volvemos las nuevas fechas Strings
            String beforeDateStr = sdf.format(startDate);
            String nextDateStr = sdf.format(endDate);
            // Convertimos las nuevas fechas Strings a dateConUtc
            Date dateStartUtc = sdf.parse(beforeDateStr);
            Date dateEndUtc = sdf.parse(nextDateStr);
            return mainAdquisitionRepository.findByCreation_dateGreaterThanAndCreation_dateLessThan(dateStartUtc,
                    dateEndUtc, "Realizada");
        } catch (ParseException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<MainAdquisitionModel> listMainAdquisition(){
        return mainAdquisitionRepository.findAll();
    }
//    @Override
//    public List<MainAdquisitionCompleteDTO> listMainAdquisition(){
//        List<Criteria> matchCriterias = new ArrayList<>();
//        List<MainAdquisitionCompleteDTO> products = listMainAdquisionTemplate(matchCriterias);
//        return products;
//    }
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
