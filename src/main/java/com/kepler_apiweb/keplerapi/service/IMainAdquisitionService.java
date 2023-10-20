package com.kepler_apiweb.keplerapi.service;

import java.util.List;

import com.kepler_apiweb.keplerapi.model.MainAdquisitionModel;

public interface IMainAdquisitionService {
   List<MainAdquisitionModel> listMainAdquisition(); 
   MainAdquisitionModel createMainAdquisition(MainAdquisitionModel adquisition);
   MainAdquisitionModel updateMainAdquisition(MainAdquisitionModel adquisition);

}




    