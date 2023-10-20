package com.kepler_apiweb.keplerapi.controller;
import com.kepler_apiweb.keplerapi.model.MainAdquisitionModel;
import com.kepler_apiweb.keplerapi.service.IMainAdquisitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/kepler/adquisitions")
public class MainAdquisitionController {
    @Autowired
    IMainAdquisitionService adquisitionService;

    @GetMapping("/")
    public ResponseEntity<List<MainAdquisitionModel>> showAdquisition() {
        return new ResponseEntity<List<MainAdquisitionModel>> (adquisitionService.listMainAdquisition(),HttpStatus.OK);
    }
}
