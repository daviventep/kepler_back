package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.model.PointTransactionModel;

import java.util.List;
import java.util.Optional;

public interface IPoint_TransactionService {
    String savePointTransaction(PointTransactionModel pointTransaction);
    List<PointTransactionModel> listPointTransaction();
    Optional<PointTransactionModel> getPointTransactionById(String pointTransactionId);
    String updatePointTransaction(PointTransactionModel pointTransaction);

}
