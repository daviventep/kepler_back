package com.kepler_apiweb.keplerapi.repository;

import com.kepler_apiweb.keplerapi.model.MainAdquisitionModel;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface IMainAdquisitionRepository extends MongoRepository<MainAdquisitionModel, Integer> {
}