package com.kepler_apiweb.keplerapi.controller;

import com.kepler_apiweb.keplerapi.DTO.AdquisitionDTO;
import com.kepler_apiweb.keplerapi.DTO.PointTransactionDTO;
import com.kepler_apiweb.keplerapi.exception.ResourceNotFoundException;
import com.kepler_apiweb.keplerapi.model.*;
import com.kepler_apiweb.keplerapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/kepler/pointTransaction")
public class PointTransactionController {
    @Autowired
    IPoint_TransactionService pointTransactionService;
    @Autowired
    IUserService userService;
    @Autowired
    IMainAdquisitionService mainAdquisitionService;

    @PostMapping("/")
    public ResponseEntity<String> createPointTransaction(@RequestBody PointTransactionModel pointTransaction) {
        PointTransactionModel.Adquisition adquisition = pointTransaction.getAdquisition();
        UserModel user = userService.getUserById(adquisition.getUser_id()).
            orElseThrow(() -> new ResourceNotFoundException(String.format("¡Error! No se encontró el usuario " +
                    "con el Id %s.", adquisition.getUser_id())));
        MainAdquisitionModel mainAdquisition =
                mainAdquisitionService.getMainAdquisitionById(adquisition.getMain_adquisition_id()).
            orElseThrow(() -> new ResourceNotFoundException(String.format("¡Error! No se encontró la " +
                    "Adquisición Principal con el Id %s.", adquisition.getMain_adquisition_id())));
        pointTransaction.getAdquisition().setTransaction_date(new Date());
        String return_string = pointTransactionService.savePointTransaction(pointTransaction);
        return new ResponseEntity<String>(return_string, HttpStatus.OK);
    }

    public List<PointTransactionDTO> maPointTransactionToDTOList(List<PointTransactionModel> pointTransactions) {
        List<PointTransactionDTO> pointTransactionDTOs = new ArrayList<>();

        for (PointTransactionModel pointTransaction : pointTransactions) {
            PointTransactionDTO pointTransactionDTO = new PointTransactionDTO();
            pointTransactionDTO.set_id(pointTransaction.get_id());
            pointTransactionDTO.setQuantity_point(pointTransaction.getQuantity_point());
            pointTransactionDTO.setAction(pointTransaction.getAction());

            AdquisitionDTO adquisitionDTO = new AdquisitionDTO();
            PointTransactionModel.Adquisition adquisition = pointTransaction.getAdquisition();

            int user_id = adquisition.getUser_id();
            adquisitionDTO.setUser_id(user_id);
            UserModel user = userService.getUserById(user_id).orElse(null);
            if (user != null) {
                adquisitionDTO.setFirst_name(user.getFirst_name());
                adquisitionDTO.setLast_name(user.getLast_name());
                adquisitionDTO.setIdentification(user.getIdentification());
            } else {
                adquisitionDTO.setFirst_name(null);
                adquisitionDTO.setLast_name(null);
                adquisitionDTO.setIdentification(0);

            }
            adquisitionDTO.setMain_adquisition_id(adquisition.getMain_adquisition_id());

            pointTransactionDTO.setAdquisition(adquisitionDTO);
            pointTransactionDTOs.add(pointTransactionDTO);
        }

        return pointTransactionDTOs;
    }
    @GetMapping("/")
    public ResponseEntity<List<PointTransactionDTO>> showPointTransactions() {
        List<PointTransactionModel> pointTransactions = pointTransactionService.listPointTransaction();
        List<PointTransactionDTO> pointTransactionDTOs = maPointTransactionToDTOList(pointTransactions);
        return new ResponseEntity<>(pointTransactionDTOs, HttpStatus.OK);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<PointTransactionDTO> filterPointTransactionById(@PathVariable int id) {
        PointTransactionModel pointTransaction = pointTransactionService.getPointTransactionById(id).
                orElseThrow(() -> new ResourceNotFoundException(String.format("¡Error! No se encontró la " +
                        "transacción de puntos con el Id %s.", id)));
        List<PointTransactionModel> singlePointTransactionList = new ArrayList<>();
        singlePointTransactionList.add(pointTransaction);
        List<PointTransactionDTO> pointTransactionDTOS = maPointTransactionToDTOList(singlePointTransactionList);

        if (!pointTransactionDTOS.isEmpty()) {
            return ResponseEntity.ok(pointTransactionDTOS.get(0));
        } else {
            throw new ResourceNotFoundException(String.format("¡Error! No se encontró la transacción de puntos con" +
                    " el Id %s.", id));
        }
    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<String> updateProductById(@PathVariable String id, @RequestBody ProductModel detailsProduct) {
//        ProductModel product = productService.getProductById(id).
//                orElseThrow(() -> new RecursoNoEncontradoException(String.format("¡Error! No se encontró el producto con el Id %s.", id)));
//        if (detailsProduct.getCategory_id() != null && !detailsProduct.getCategory_id().toString().isEmpty()) {
//            CategoryModel category = categoryService.getCategoryById(product.getCategory_id().toString()).
//                    orElseThrow(() -> new RecursoNoEncontradoException(String.format("¡Error! No se encontró la categoría con el Id %s.", product.getCategory_id())));
//            product.setCategory_id(detailsProduct.getCategory_id());
//        }
//        if (detailsProduct.getName() != null && !detailsProduct.getName().isEmpty()) {
//            product.setName(detailsProduct.getName());
//        }
//        if (detailsProduct.getMoney_unit_price() != null) {
//            product.setMoney_unit_price(detailsProduct.getMoney_unit_price());
//        }
//        if (detailsProduct.getPoint_unit_price() != null) {
//            product.setPoint_unit_price(detailsProduct.getPoint_unit_price());
//        }
//        if (detailsProduct.getDescription() != null && !detailsProduct.getDescription().isEmpty()) {
//            product.setDescription(detailsProduct.getDescription());
//        }
//        if (detailsProduct.getQuantity() != null) {
//            product.setQuantity(detailsProduct.getQuantity());
//        }
//        if (detailsProduct.getWeight() != null) {
//            product.setWeight(detailsProduct.getWeight());
//        }
//        if (detailsProduct.getMeasure_unit() != null && !detailsProduct.getMeasure_unit().isEmpty()) {
//            product.setMeasure_unit(detailsProduct.getMeasure_unit());
//        }
//        if (detailsProduct.getStatus() != null) {
//            product.setStatus(detailsProduct.getStatus());
//        }
//        if (detailsProduct.getImage_product() != null && !detailsProduct.getImage_product().isEmpty()) {
//            product.setImage_product(detailsProduct.getImage_product());
//        }
//        return new ResponseEntity<String>(productService.updateProduct(product), HttpStatus.OK);
//    }
}
