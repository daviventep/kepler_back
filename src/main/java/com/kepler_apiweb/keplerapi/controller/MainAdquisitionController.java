package com.kepler_apiweb.keplerapi.controller;
import com.kepler_apiweb.keplerapi.exception.RecursoNoEncontradoException;
import com.kepler_apiweb.keplerapi.model.MainAdquisitionModel;
import com.kepler_apiweb.keplerapi.model.ProductModel;
import com.kepler_apiweb.keplerapi.model.UserModel;
import com.kepler_apiweb.keplerapi.service.IMainAdquisitionService;
import com.kepler_apiweb.keplerapi.service.IProductService;
import com.kepler_apiweb.keplerapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kepler/adquisitions")
public class MainAdquisitionController {
    @Autowired
    IMainAdquisitionService mainAdquisitionService;
    @Autowired
    IUserService userService;
    @Autowired
    IProductService productService;

    @PostMapping("/add")
    public ResponseEntity<String> saveMainAdquisition(@RequestBody MainAdquisitionModel mainAdquisition) {
        UserModel user = userService.getUserById(mainAdquisition.getUser_id().toString()).
                orElseThrow(() -> new RecursoNoEncontradoException(String.format("¡Error! No se ha encontrado el usuario con el Id %s", mainAdquisition.getUser_id().toString())));
        for (MainAdquisitionModel.AdquisitionDetail detail : mainAdquisition.getAdquisition_details()) {
            if ((detail.getProduct_id() == null) || (detail.getProduct_id() == "")) {
                throw new RecursoNoEncontradoException(String.format("¡Error! No se recibió un producto en los " +
                        "detalles de adquisición"));
            }
            ProductModel product = productService.getProductById(detail.getProduct_id().toString()).
                    orElseThrow(() -> new RecursoNoEncontradoException(String.format("¡Error! No se ha encontrado el " +
                            "producto con el Id %s", detail.getProduct_id().toString())));
        }
        mainAdquisitionService.saveMainAdquisition(mainAdquisition);
        return new ResponseEntity<String>(mainAdquisitionService.saveMainAdquisition(mainAdquisition), HttpStatus.OK);
    }


    @GetMapping("/")
    public ResponseEntity<List<MainAdquisitionModel>> showAdquisition() {
        return new ResponseEntity<List<MainAdquisitionModel>> (mainAdquisitionService.listMainAdquisition(),HttpStatus.OK);
    }
}
