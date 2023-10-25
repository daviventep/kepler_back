package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.model.CategoryModel;
import com.kepler_apiweb.keplerapi.model.PointTransactionModel;
import com.kepler_apiweb.keplerapi.repository.ICategoryRepository;
import com.kepler_apiweb.keplerapi.repository.IPointTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointTransactionServiceImp implements IPoinTransactionService{
    @Autowired
    IPointTransactionRepository pointTransactionRepository;


    @Override
    public String createTransaction(PointTransactionModel pointTransaction) {
         pointTransactionRepository.save(pointTransaction);
        return "El pointTransaction fue creado con exito";
    }

    @Override
    public List<PointTransactionModel> listTransaction() {
        return pointTransactionRepository.findAll();
    }
}
