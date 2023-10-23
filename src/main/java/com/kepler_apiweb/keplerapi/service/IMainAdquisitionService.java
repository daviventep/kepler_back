package com.kepler_apiweb.keplerapi.service;

import java.util.List;
import java.util.Optional;

import com.kepler_apiweb.keplerapi.model.MainAdquisitionModel;

public interface IMainAdquisitionService {
   String saveMainAdquisition(MainAdquisitionModel mainAdquisition);
   List<MainAdquisitionModel> listMainAdquisition();
   Optional<MainAdquisitionModel> getMainAdquisitionById(String mainAdquisitionId);
   List<MainAdquisitionModel> getMainAdquisitionsByUser(String userId);

   List validateAdquisitionsByUserStatus(String userId, String statusType);

   MainAdquisitionModel updateMainAdquisition(MainAdquisitionModel adquisition);

   String addAdquisition(MainAdquisitionModel mainAdquisition);
}




    