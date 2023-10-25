package com.kepler_apiweb.keplerapi.controller;

import com.kepler_apiweb.keplerapi.model.PointTransactionModel;
import com.kepler_apiweb.keplerapi.service.IPoinTransactionService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/kepler/transaction")
public class PointTransactionController {
    @Autowired
    IPoinTransactionService pointTransactionService;
    @Autowired
    //IMain_AdquisitionServicce main_AdquisitionServicce
    @GetMapping("/")
    public ResponseEntity<List<PointTransactionModel>> showTransaction() {
        return new ResponseEntity<List<PointTransactionModel>>(pointTransactionService.listTransaction(), HttpStatus.OK);
    }

    public ResponseEntity<String> createPointTransaction(@RequestBody PointTransactionModel pointTransaction){
        //recuperar Main_Adquisition

        //creamos el documento de adq
        DBObject documentoAdq = new BasicDBObject();
        documentoAdq.put("user_id", #####);
        documentoMatricula.put("nota", nota);

        return ResponseEntity.ok(pointTransactionService.createTransaction(pointTransaction));

    }
}
