package com.kepler_apiweb.keplerapi.repository;

import com.kepler_apiweb.keplerapi.DTO.MainAdquisitionCompleteDTO;
import com.kepler_apiweb.keplerapi.model.MainAdquisitionModel;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IMainAdquisitionRepository extends MongoRepository<MainAdquisitionModel, Integer> {
    @Query(value = "{'user_id':  ?0}")
    List<MainAdquisitionModel> findByUserId(int userId);
    @Query(value = "{'user_id':  ?0, 'status': ?1}")
    List<MainAdquisitionModel> findByUserIdStatus(int userId, String statusType);
    @Query(value = "{}", sort = "{'_id' : -1}", fields = "{'_id': 1}")
    List<MainAdquisitionModel> findLastMainAdquisition();

    @Query(value = "{'_id': ?0, 'user_id': ?1, 'status': ?2}")
    Optional<Object> findBy_idAndUser_idAndStatus(int id, int user_id, String status);

    @Query(value = "{'user_id': ?0, 'status': ?1}")
    Optional<MainAdquisitionModel> findByUser_idAndStatus(int userId, String pendiente);

    // SOLUCIÓN A PREGUNTA 1
    @Aggregation({
            "{ $lookup: { from: 'User', localField: 'user_id', foreignField: '_id', as: 'user' } }",
            "{ $unwind: '$user' }",
            "{$match: {user_id: ?0, status: ?1, 'user.profiles.type': ?2}}",
            "{ $lookup: { from: 'Product', localField: 'adquisition_details.product_id', foreignField: '_id', as: 'product' } }",
            "{ $unwind: '$product' }",
            "{ $project: { _id: 1, money_total_value: 1, point_total_value: 1, creation_date: 1, " +
                    "delivery_date: 1, user_id: 1, first_name: '$user.first_name', last_name: '$user.last_name', " +
                    "identification: '$user.identification', status: 1, " +
                    "adquisition_details: {" +
                        "product_id: 1, product_name: '$product.name', money_unit_value: 1, point_unit_value: 1, " +
                        "quantity: 1, description: 1} } }",
            "{ $sort: { creation_date: 1 } }"
    })
    List<MainAdquisitionCompleteDTO> findSalesByUserIdOrderByDate(int userId, String status, String typeProfile);

    // SOLUCIÓN A PREGUNTA 2
    @Aggregation({
            "{$match: {creation_date: {$gte: ?0, $lt: ?1}, status: ?2}}",
            "{ $lookup: { from: 'User', localField: 'user_id', foreignField: '_id', as: 'user' } }",
            "{ $unwind: '$user' }",
            "{ $project: { _id: 1, money_total_value: 1, point_total_value: 1, creation_date: 1, " +
                    "delivery_date: 1, user_id: 1, first_name: '$user.first_name', last_name: '$user.last_name', " +
                    "identification: '$user.identification', status: 1, adquisition_details: 1 } }"
    })
    List<MainAdquisitionCompleteDTO> findByCreation_dateGreaterThanAndCreation_dateLessThan(Date dateStart, Date dateEnd, String status);


}