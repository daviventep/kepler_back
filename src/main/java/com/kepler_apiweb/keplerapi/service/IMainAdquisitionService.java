package com.kepler_apiweb.keplerapi.service;

import java.util.List;
import java.util.Optional;

import com.kepler_apiweb.keplerapi.DTO.MainAdquisitionPurchaseDTO;
import com.kepler_apiweb.keplerapi.model.MainAdquisitionModel;

public interface IMainAdquisitionService {
   String saveMainAdquisition(MainAdquisitionModel mainAdquisition);

   String makePurchase(MainAdquisitionPurchaseDTO makeAdquisition);

   List<MainAdquisitionModel> listMainAdquisition();
   Optional<MainAdquisitionModel> getMainAdquisitionById(int mainAdquisitionId);

   List validateAdquisitionsByUserStatus(int userId, String statusType);

   MainAdquisitionModel updateMainAdquisition(MainAdquisitionModel adquisition);

   // Filtrar Main Adquisition por User
   List<MainAdquisitionModel> getMainAdquisitionsByUser(int userId);

   int getNextId();
}




    