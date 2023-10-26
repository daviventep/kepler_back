package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.model.PointTransactionModel;

import java.util.List;
import java.util.Optional;

public interface IPoint_TransactionService {
    String savePointTransaction(PointTransactionModel pointTransaction);
    List<PointTransactionModel> listPointTransaction();

    // Filtrar un pointTransaction por Id
    Optional<PointTransactionModel> getPointTransactionById(int pointTransactionId);

    String updatePointTransaction(PointTransactionModel pointTransaction);

}
