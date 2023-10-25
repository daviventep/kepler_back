package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.model.CategoryModel;
import com.kepler_apiweb.keplerapi.model.PointTransactionModel;

import java.util.List;

public interface IPoinTransactionService {
    String createTransaction(PointTransactionModel pointTransaction);
    List<PointTransactionModel> listTransaction();
}
