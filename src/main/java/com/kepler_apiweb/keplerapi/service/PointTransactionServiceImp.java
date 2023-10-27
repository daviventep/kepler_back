package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.model.CategoryModel;
import com.kepler_apiweb.keplerapi.model.PointTransactionModel;
import com.kepler_apiweb.keplerapi.repository.IPoint_TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PointTransactionServiceImp implements IPoint_TransactionService {
    @Autowired
    IPoint_TransactionRepository pointTransactionRepository;
    // Crear pointTransaction
    @Override
    public String savePointTransaction(PointTransactionModel pointTransaction) {
        pointTransactionRepository.save(pointTransaction);
        return String.format("La transacción de puntos ha sido creada con el Id %s.", pointTransaction.get_id());
    }
    // Listar pointTransactions
    @Override
    public List<PointTransactionModel> listPointTransaction() {
         List<PointTransactionModel> pointTransactions = pointTransactionRepository.findAll();
         return pointTransactions;
    }
    // Filtrar un pointTransaction por Id
    @Override
    public Optional<PointTransactionModel> getPointTransactionById(int pointTransactionId) {
        return pointTransactionRepository.findById(pointTransactionId);
    }
    @Override
    public int getNextId() {
        int return_num;
        List<PointTransactionModel> listPointTransactions = pointTransactionRepository.findLastPointTransaction();
        if (!listPointTransactions.isEmpty() && listPointTransactions.get(0) != null) {
            return_num = listPointTransactions.get(0).get_id() + 1;
        } else {
            return_num = 1;
        }
        return return_num;
    }
    // Actualizar pointTransaction por Id
    @Override
    public String updatePointTransaction(PointTransactionModel pointTransaction) {
        pointTransactionRepository.save(pointTransaction);
        return String.format("La transacción con Id %s se ha actualizado de forma exisosa.", pointTransaction.get_id());
    }
}