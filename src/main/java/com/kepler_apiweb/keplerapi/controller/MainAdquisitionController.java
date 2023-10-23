package com.kepler_apiweb.keplerapi.controller;
import com.kepler_apiweb.keplerapi.DTO.AdquisitionDetailsDTO;
import com.kepler_apiweb.keplerapi.DTO.MainAdquisitionCompleteDTO;
import com.kepler_apiweb.keplerapi.DTO.ProductWithCategoryDTO;
import com.kepler_apiweb.keplerapi.exception.RecursoNoEncontradoException;
import com.kepler_apiweb.keplerapi.model.CategoryModel;
import com.kepler_apiweb.keplerapi.model.MainAdquisitionModel;
import com.kepler_apiweb.keplerapi.model.ProductModel;
import com.kepler_apiweb.keplerapi.model.UserModel;
import com.kepler_apiweb.keplerapi.service.IMainAdquisitionService;
import com.kepler_apiweb.keplerapi.service.IProductService;
import com.kepler_apiweb.keplerapi.service.IUserService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        String returned;
        if ((mainAdquisition.getUser_id() == null) || (mainAdquisition.getUser_id().toString().isEmpty())) {
            throw new RecursoNoEncontradoException("¡Error! No se recibió el Id del usuario.");
        }
        UserModel user = userService.getUserById(mainAdquisition.getUser_id().toString()).
                orElseThrow(() -> new RecursoNoEncontradoException(String.format("¡Error! No se ha encontrado el usuario con el Id %s", mainAdquisition.getUser_id().toString())));
        for (MainAdquisitionModel.AdquisitionDetail detail : mainAdquisition.getAdquisition_details()) {
            if ((detail.getProduct_id() == null) || (detail.getProduct_id().toString().isEmpty())) {
                throw new RecursoNoEncontradoException(String.format("¡Error! No se recibió un producto en los " +
                        "detalles de adquisición"));
            }
            ProductModel product = productService.getProductById(detail.getProduct_id().toString()).
                    orElseThrow(() -> new RecursoNoEncontradoException(String.format("¡Error! No se ha encontrado el " +
                            "producto con el Id %s", detail.getProduct_id().toString())));
        }
        Date creationDate = new Date();
        mainAdquisition.setCreation_date(creationDate);
        mainAdquisition.setStatus("Pendiente");

        // Validar si ya tiene uno abierto o si aún no
        List<MainAdquisitionModel> recordPendienteMainAdquisition =
                mainAdquisitionService.validateAdquisitionsByUserStatus(mainAdquisition.getUser_id().toString(),
                        "Pendiente");

        if (recordPendienteMainAdquisition.size() > 0) {
            for (MainAdquisitionModel.AdquisitionDetail adquisitionDetail : mainAdquisition.getAdquisition_details()) {
                ObjectId objectIdProductId = new ObjectId(adquisitionDetail.getProduct_id().toString());
                Optional<MainAdquisitionModel.AdquisitionDetail> matchingDetail =
                        recordPendienteMainAdquisition.get(0).getAdquisition_details().stream().filter(
                                detail -> detail.getProduct_id().equals(objectIdProductId) && detail.getDescription().equals(adquisitionDetail.getDescription())).findFirst();
                if (matchingDetail.isPresent()) {
                    MainAdquisitionModel.AdquisitionDetail detailRecord = matchingDetail.get();
                    int indexDetail =
                            recordPendienteMainAdquisition.get(0).getAdquisition_details().indexOf(detailRecord);
                    detailRecord.setQuantity(detailRecord.getQuantity() + adquisitionDetail.getQuantity());

                    recordPendienteMainAdquisition.get(0).getAdquisition_details().set(indexDetail, detailRecord);
                } else {
                    recordPendienteMainAdquisition.get(0).getAdquisition_details().add(adquisitionDetail);
                }
            }
            returned = mainAdquisitionService.saveMainAdquisition(recordPendienteMainAdquisition.get(0));
        } else {
            returned = mainAdquisitionService.saveMainAdquisition(mainAdquisition);
        }
        return new ResponseEntity<String>(returned, HttpStatus.OK);
    }

    public List<MainAdquisitionCompleteDTO> mapMainAdquisitionModelToDTOList(List<MainAdquisitionModel> mainAdquisitions) {
        List<MainAdquisitionCompleteDTO> mainAdquisitionDTOs = new ArrayList<>();

        for (MainAdquisitionModel mainAdquisition : mainAdquisitions) {
            MainAdquisitionCompleteDTO mainAdquisitionDTO = new MainAdquisitionCompleteDTO();
            mainAdquisitionDTO.set_id(mainAdquisition.get_id());
            mainAdquisitionDTO.setMoney_total_value(mainAdquisition.getMoney_total_value());
            mainAdquisitionDTO.setPoint_total_value(mainAdquisition.getPoint_total_value());
            mainAdquisitionDTO.setCreation_date(mainAdquisition.getCreation_date());
            mainAdquisitionDTO.setDelivery_date(mainAdquisition.getDelivery_date());
            mainAdquisitionDTO.setUser_id(mainAdquisition.getUser_id().toString());

            String userId = mainAdquisition.getUser_id().toString();
            UserModel user = userService.getUserById(userId)
                    .orElse(null);
            if (user != null) {
                mainAdquisitionDTO.setFirst_name(user.getFirst_name());
                mainAdquisitionDTO.setLast_name(user.getLast_name());
                mainAdquisitionDTO.setIdentification(Integer.toString(user.getIdentification()));
            } else {
                mainAdquisitionDTO.setFirst_name(null);
                mainAdquisitionDTO.setLast_name(null);
                mainAdquisitionDTO.setIdentification(null);
            }

            mainAdquisitionDTO.setStatus(mainAdquisition.getStatus());

            List<AdquisitionDetailsDTO> adquisitionDetailsDTOs = new ArrayList<>();
            for (MainAdquisitionModel.AdquisitionDetail adquisitionDetail : mainAdquisition.getAdquisition_details()) {
                AdquisitionDetailsDTO adquisitionDetailDTO = new AdquisitionDetailsDTO();
                adquisitionDetailDTO.setProduct_id(adquisitionDetail.getProduct_id().toString());

                String productId = adquisitionDetail.getProduct_id().toString();
                ProductModel
                        product = productService.getProductById(productId).orElse(null);
                if (product != null) {
                    adquisitionDetailDTO.setProduct_name(product.getName());
                } else {
                    adquisitionDetailDTO.setProduct_name(null);
                }

                adquisitionDetailDTO.setMoney_unit_value(adquisitionDetail.getMoney_unit_value());
                adquisitionDetailDTO.setPoint_unit_value(adquisitionDetail.getPoint_unit_value());
                adquisitionDetailDTO.setQuantity(adquisitionDetail.getQuantity());
                adquisitionDetailDTO.setDescription(adquisitionDetail.getDescription());

                adquisitionDetailsDTOs.add(adquisitionDetailDTO);
            }
            mainAdquisitionDTO.setAdquisition_details(adquisitionDetailsDTOs);

            mainAdquisitionDTOs.add(mainAdquisitionDTO);
        }
        return mainAdquisitionDTOs;
    }
    @GetMapping("/")
    public ResponseEntity<List<MainAdquisitionCompleteDTO>> showAdquisition() {
        List<MainAdquisitionModel> mainAdquisitions = mainAdquisitionService.listMainAdquisition();
        List<MainAdquisitionCompleteDTO> mainAdquisitionsDTOs = mapMainAdquisitionModelToDTOList(mainAdquisitions);
        return new ResponseEntity<> (mainAdquisitionsDTOs, HttpStatus.OK);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<MainAdquisitionCompleteDTO> filterMainAdquisitionById(@PathVariable String id) {
        MainAdquisitionModel mainAdquisition = mainAdquisitionService.getMainAdquisitionById(id).
                orElseThrow(() -> new RecursoNoEncontradoException(String.format("¡Error! No se encontró la " +
                        "Adquisición Principal " +
                        "con el Id %s.", id)));
        List<MainAdquisitionModel> singleMainAdquitionList = new ArrayList<>();
        singleMainAdquitionList.add(mainAdquisition);
        List<MainAdquisitionCompleteDTO> mainAdqusitionDTOs = mapMainAdquisitionModelToDTOList(singleMainAdquitionList);

        if (!mainAdqusitionDTOs.isEmpty()) {
            return ResponseEntity.ok(mainAdqusitionDTOs.get(0));
        } else {
            throw new RecursoNoEncontradoException(String.format("¡Error! No se encontró la Adquisición Principal con" +
                            " el Id %s.",
                    id));
        }
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<List<MainAdquisitionCompleteDTO>> showProductByCategory(@PathVariable String id) {
        List<MainAdquisitionModel> mainAdquisitions = mainAdquisitionService.getMainAdquisitionsByUser(id);
        List<MainAdquisitionCompleteDTO> mainAdquisitionsDTO = mapMainAdquisitionModelToDTOList(mainAdquisitions);
        return new ResponseEntity<>(mainAdquisitionsDTO, HttpStatus.OK);
    }
}
