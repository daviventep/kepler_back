package com.kepler_apiweb.keplerapi.repository;

import com.kepler_apiweb.keplerapi.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IUserRepository extends MongoRepository<UserModel, Integer> {

}
