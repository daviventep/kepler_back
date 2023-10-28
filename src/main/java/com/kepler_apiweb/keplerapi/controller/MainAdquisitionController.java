package com.kepler_apiweb.keplerapi.controller;
import com.kepler_apiweb.keplerapi.DTO.AdquisitionDetailsDTO;
import com.kepler_apiweb.keplerapi.DTO.MainAdquisitionCompleteDTO;
import com.kepler_apiweb.keplerapi.DTO.MainAdquisitionDeliveryDTO;
import com.kepler_apiweb.keplerapi.DTO.MainAdquisitionPurchaseDTO;
import com.kepler_apiweb.keplerapi.exception.ResourceExist;
import com.kepler_apiweb.keplerapi.exception.ResourceNotFoundException;
import com.kepler_apiweb.keplerapi.model.MainAdquisitionModel;
import com.kepler_apiweb.keplerapi.model.PointTransactionModel;
import com.kepler_apiweb.keplerapi.model.ProductModel;
import com.kepler_apiweb.keplerapi.model.UserModel;
import com.kepler_apiweb.keplerapi.service.IMainAdquisitionService;
import com.kepler_apiweb.keplerapi.service.IPoint_TransactionService;
import com.kepler_apiweb.keplerapi.service.IProductService;
import com.kepler_apiweb.keplerapi.service.IUserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
    @Autowired
    IPoint_TransactionService pointTransactionService;

    @PostMapping("/add")
    public ResponseEntity<String> saveMainAdquisition(@RequestBody MainAdquisitionModel mainAdquisition) {
        String returned;
        if (mainAdquisition.get_id() == 0) {
            throw new ResourceNotFoundException(String.format("¡Error! No se recibió un Id de la adquisición."));
        }
        String hasErrorMainAdquisitionExist =
                mainAdquisitionService.getMainAdquisitionByIdAndUserId(mainAdquisition.get_id(), mainAdquisition.getUser_id());
        if (hasErrorMainAdquisitionExist != "") {
            throw new ResourceExist(hasErrorMainAdquisitionExist);
        }
        if (mainAdquisition.getUser_id() == 0) {
            throw new ResourceNotFoundException("¡Error! No se recibió el Id del usuario.");
        }
        UserModel user = userService.getUserById(mainAdquisition.getUser_id()).
                orElseThrow(() -> new ResourceNotFoundException(String.format("¡Error! No se ha encontrado el usuario" +
                        " con el Id %d", mainAdquisition.getUser_id())));
        for (MainAdquisitionModel.AdquisitionDetail detail : mainAdquisition.getAdquisition_details()) {
            if (detail.getProduct_id() == 0) {
                throw new ResourceNotFoundException(String.format("¡Error! No se recibió un producto en los " +
                        "detalles de adquisición"));
            }
            if (detail.getQuantity() == 0) {
                throw new IllegalArgumentException("¡Error! La cantidad (quantity) no puede ser cero para el producto " + detail.getProduct_id());
            }
            ProductModel product = productService.getProductById(detail.getProduct_id()).
                    orElseThrow(() -> new ResourceNotFoundException(String.format("¡Error! No se ha encontrado el " +
                            "producto con el Id %d", detail.getProduct_id())));
            if (detail.getQuantity() > product.getQuantity()) {
                throw new ResourceExist(String.format("La cantidad del producto %s con iD %d es mayor a la que se " +
                        "tiene en stock (%d).", product.getName(), detail.getProduct_id(), product.getQuantity()));
            }
        }
        Date creationDate = new Date();
        mainAdquisition.setCreation_date(creationDate);
        mainAdquisition.setStatus("Pendiente");

        // Validar si ya tiene uno abierto o si aún no
        List<MainAdquisitionModel> recordPendienteMainAdquisition =
                mainAdquisitionService.validateAdquisitionsByUserStatus(mainAdquisition.getUser_id(),
                        "Pendiente");

        if (recordPendienteMainAdquisition.size() > 0) {
            for (MainAdquisitionModel.AdquisitionDetail adquisitionDetail : mainAdquisition.getAdquisition_details()) {
                Optional<MainAdquisitionModel.AdquisitionDetail> matchingDetail =
                        recordPendienteMainAdquisition.get(0).getAdquisition_details().stream().filter(
                                detail -> detail.getProduct_id() == adquisitionDetail.getProduct_id() && detail.getDescription().equals(adquisitionDetail.getDescription())).findFirst();
                if (matchingDetail.isPresent()) {
                    MainAdquisitionModel.AdquisitionDetail detailRecord = matchingDetail.get();
                    int indexDetail =
                            recordPendienteMainAdquisition.get(0).getAdquisition_details().indexOf(detailRecord);

                    ProductModel product = productService.getProductById(adquisitionDetail.getProduct_id()).
                            orElseThrow(() -> new ResourceNotFoundException(String.format("¡Error! No se ha encontrado el " +
                                    "producto con el Id %d", adquisitionDetail.getProduct_id())));
                    if (detailRecord.getQuantity() + adquisitionDetail.getQuantity() > product.getQuantity()) {
                        throw new ResourceExist(String.format("La cantidad del producto %s con iD %d es mayor a la que se " +
                                "tiene en stock (%d).", product.getName(), adquisitionDetail.getProduct_id(), product.getQuantity()));
                    }
                    detailRecord.setQuantity(detailRecord.getQuantity() + adquisitionDetail.getQuantity());

                    recordPendienteMainAdquisition.get(0).getAdquisition_details().set(indexDetail, detailRecord);
                } else {
                    recordPendienteMainAdquisition.get(0).getAdquisition_details().add(adquisitionDetail);
                }
            }
            returned = mainAdquisitionService.saveMainAdquisition(recordPendienteMainAdquisition.get(0), false, true);
        } else {
            returned = mainAdquisitionService.saveMainAdquisition(mainAdquisition, false, true);
        }
        return new ResponseEntity<String>(returned, HttpStatus.OK);
    }

    @PostMapping("/buy")
    public ResponseEntity<String> makePurchase(@RequestBody MainAdquisitionPurchaseDTO adquisitionPurchase) {
        String return_process;
        Boolean receivedPoinsBirthday = false;
        int pointsUsed = 0;
        double moneyUsed = 0;
        double totalValuePerDetail = 0;
        int pointsTotal;
        boolean usePoints = adquisitionPurchase.getUse_points();
        int pointsEarnedPer300PointsSpent = 40;
        int pointsEarnedPer30000MoneySpent = 30;
        int pointsEarnedForBirthday = 500;
        if ((adquisitionPurchase.getUse_points() == null) || (adquisitionPurchase.getUser_id() == 0)) {
            throw new ResourceNotFoundException("¡Error! Faltó enviar si usar los puntos o el Id del Usuario");
        }
        UserModel user = userService.getUserById(adquisitionPurchase.getUser_id()).
                orElseThrow(() -> new ResourceNotFoundException(String.format("¡Error! No se ha encontrado el " +
                        "usuario con el Id %s", adquisitionPurchase.getUser_id())));
        List<MainAdquisitionModel> recordsPendienteMainAdquisition =
                mainAdquisitionService.validateAdquisitionsByUserStatus(adquisitionPurchase.getUser_id(),
                        "Pendiente");
        Date dateCurrent = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateCurrent);
        int monthCurrent = calendar.get(Calendar.MONTH) + 1;
        int dayCurrent = calendar.get(Calendar.DAY_OF_MONTH);
        Instant birthDayInstant = user.getBirth_date().toInstant();
        ZonedDateTime zonedDateTime = birthDayInstant.atZone(ZoneOffset.UTC);
//        calendar.setTime(birthDay);
        int monthBirthday = zonedDateTime.getMonthValue();
        int dayBirthday = zonedDateTime.getDayOfMonth();
        pointsTotal = user.getPoints();
        // Si ese día cumple años.
        if ((monthCurrent == monthBirthday) && (dayCurrent == dayBirthday)) {
            receivedPoinsBirthday = true;
            pointsTotal += pointsEarnedForBirthday;
        }

        if (recordsPendienteMainAdquisition.size() > 0) {
            if (recordsPendienteMainAdquisition.get(0).getAdquisition_details().size() == 0) {
                throw new ResourceNotFoundException("No has añadido productos para comprar.");
            }
            List productsBought = new ArrayList<>();
            for (MainAdquisitionModel.AdquisitionDetail adquisitionDetail : recordsPendienteMainAdquisition.get(0).getAdquisition_details()) {
                ProductModel product = productService.getProductById(adquisitionDetail.getProduct_id()).
                    orElseThrow(() -> new ResourceNotFoundException(String.format("¡Error! No se encontró un " +
                            "producto con el Id %s.", adquisitionDetail.getProduct_id())));
                if (adquisitionDetail.getQuantity() > product.getQuantity()) {
                    throw new ResourceExist(String.format("No hay suficiente Stock con el producto %s que tiene %d y " +
                            "requiere %d.", product.getName(), product.getQuantity(), adquisitionDetail.getQuantity()));
                }
                product.setQuantity(product.getQuantity() - adquisitionDetail.getQuantity());
                int indexDetail =
                        recordsPendienteMainAdquisition.get(0).getAdquisition_details().indexOf(adquisitionDetail);
                adquisitionDetail.setMoney_unit_value(product.getMoney_unit_price());
                adquisitionDetail.setPoint_unit_value(product.getPoint_unit_price());
                if (usePoints == true) {
                    totalValuePerDetail = adquisitionDetail.getQuantity() * product.getPoint_unit_price();
                    if (pointsTotal > 0 && pointsTotal >= totalValuePerDetail) {
                        pointsTotal -= totalValuePerDetail;
                        pointsUsed += totalValuePerDetail;
                    } else {
                        moneyUsed += adquisitionDetail.getQuantity() * product.getMoney_unit_price();
                    }
                } else {
                    moneyUsed += adquisitionDetail.getQuantity() * product.getMoney_unit_price();
                }
                productsBought.add(product);
                recordsPendienteMainAdquisition.get(0).getAdquisition_details().set(indexDetail, adquisitionDetail);
            }
            // Suma de puntos que recibió
            int pointsSpentToGivePoints = (int) Math.round(pointsUsed / 300) * pointsEarnedPer300PointsSpent;
            int moneysSpentToGivePoints = (int) Math.round(moneyUsed / 30000) * pointsEarnedPer30000MoneySpent;
            int pointsTotalToGive = pointsSpentToGivePoints + moneysSpentToGivePoints;
            // Modificar puntos con los que quedó finalmente
            user.setPoints(pointsTotal + pointsTotalToGive);
            userService.saveUser(user);
            // Modificar datos del Main_Adquisition
            recordsPendienteMainAdquisition.get(0).setPoint_total_value(pointsUsed);
            recordsPendienteMainAdquisition.get(0).setMoney_total_value(moneyUsed);
            recordsPendienteMainAdquisition.get(0).setStatus("Realizada");
            mainAdquisitionService.saveMainAdquisition(recordsPendienteMainAdquisition.get(0), false, true);

            // ---- Crear registros de Point_Transaction
            // Objeto Adquisition que guarda Point_Transaction
            PointTransactionModel.Adquisition adquisitionObject = new PointTransactionModel.Adquisition();
            adquisitionObject.setMain_adquisition_id(recordsPendienteMainAdquisition.get(0).get_id());
            adquisitionObject.setUser_id(recordsPendienteMainAdquisition.get(0).getUser_id());
            adquisitionObject.setTransaction_date(new Date());
            adquisitionObject.setMain_adquisition_id(recordsPendienteMainAdquisition.get(0).get_id());
            // Reporte de gasto de puntos por compra
            PointTransactionModel spentPointsRecord = new PointTransactionModel();
            int idPointTransactionToAssign = pointTransactionService.getNextId();
            spentPointsRecord.set_id(idPointTransactionToAssign);
            spentPointsRecord.setQuantity_point(pointsUsed);
            spentPointsRecord.setAction("Compra");
            spentPointsRecord.setAdquisition(adquisitionObject);
            pointTransactionService.savePointTransaction(spentPointsRecord);
            if (receivedPoinsBirthday == true) {
                // Reporte de recepción de puntos por cumpleaños
                PointTransactionModel birthdayPointsRecord = new PointTransactionModel();
                idPointTransactionToAssign = pointTransactionService.getNextId();
                birthdayPointsRecord.set_id(idPointTransactionToAssign);
                birthdayPointsRecord.setQuantity_point(pointsEarnedForBirthday);
                birthdayPointsRecord.setAction("Cumpleaños");
                birthdayPointsRecord.setAdquisition(adquisitionObject);
                pointTransactionService.savePointTransaction(birthdayPointsRecord);
            }
            // Reporte de recepción de puntos por compra
            PointTransactionModel pointsReceivedRecord = new PointTransactionModel();
            idPointTransactionToAssign = pointTransactionService.getNextId();
            pointsReceivedRecord.set_id(idPointTransactionToAssign);
            pointsReceivedRecord.setQuantity_point(pointsTotalToGive);
            pointsReceivedRecord.setAction("Recepción");
            pointsReceivedRecord.setAdquisition(adquisitionObject);
            pointTransactionService.savePointTransaction(pointsReceivedRecord);

            for (int i = 0; i < productsBought.size(); i++) {
                ProductModel product = (ProductModel) productsBought.get(i);
                productService.saveProduct(product);
            }

            String formatPointsSpent = NumberFormat.getNumberInstance(Locale.US).format(pointsUsed);
            String formatMoneySpent = NumberFormat.getNumberInstance(Locale.US).format(moneyUsed);

            return new ResponseEntity<String>(String.format("Realizó una compra por %s puntos y %s COP. Muchas " +
                    "gracias por elegirnos %s %s.", formatPointsSpent, formatMoneySpent, user.getFirst_name(),
                    user.getLast_name()),
                    HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("¡Error! No tiene productos pendientes por comprar.");
        }
    }

    @PostMapping("/delivery")
    public ResponseEntity<String> saveDeliveryStatus(@RequestBody MainAdquisitionDeliveryDTO deliveryBody) {
        MainAdquisitionModel mainAdquisition =
                mainAdquisitionService.getMainAdquisitionById(deliveryBody.get_id()).
                orElseThrow(() -> new ResourceNotFoundException(String.format("¡Error! No se ha encontrado la " +
                        "adquisición con el Id %d.", deliveryBody.get_id())));
        System.out.println(mainAdquisition.getStatus());
        if (!mainAdquisition.getStatus().equals("Realizada")) {
            throw new ResourceExist(String.format("La adquisición con iD %d no está en un estado disponible (%s).",
                    deliveryBody.get_id(), mainAdquisition.getStatus()));
        }
        mainAdquisition.setStatus("Entregado");
        mainAdquisition.setDelivery_date(new Date());
        String return_text = mainAdquisitionService.updateStatus(mainAdquisition);

        return new ResponseEntity<String>(return_text, HttpStatus.OK);
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
            mainAdquisitionDTO.setUser_id(mainAdquisition.getUser_id());

            int userId = mainAdquisition.getUser_id();
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
                adquisitionDetailDTO.setProduct_id(adquisitionDetail.getProduct_id());

                int productId = adquisitionDetail.getProduct_id();
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
    public ResponseEntity<List<MainAdquisitionModel>> showAdquisition() {
        List<MainAdquisitionModel> mainAdquisitions = mainAdquisitionService.listMainAdquisition();
        return new ResponseEntity<> (mainAdquisitions, HttpStatus.OK);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<MainAdquisitionCompleteDTO> filterMainAdquisitionById(@PathVariable int id) {
        MainAdquisitionModel mainAdquisition = mainAdquisitionService.getMainAdquisitionById(id).
                orElseThrow(() -> new ResourceNotFoundException(String.format("¡Error! No se encontró la " +
                        "Adquisición Principal " +
                        "con el Id %s.", id)));
        List<MainAdquisitionModel> singleMainAdquitionList = new ArrayList<>();
        singleMainAdquitionList.add(mainAdquisition);
        List<MainAdquisitionCompleteDTO> mainAdqusitionDTOs = mapMainAdquisitionModelToDTOList(singleMainAdquitionList);

        if (!mainAdqusitionDTOs.isEmpty()) {
            return ResponseEntity.ok(mainAdqusitionDTOs.get(0));
        } else {
            throw new ResourceNotFoundException(String.format("¡Error! No se encontró la Adquisición Principal con" +
                            " el Id %s.",
                    id));
        }
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<List<MainAdquisitionCompleteDTO>> showMainAdquisitionComplete(@PathVariable int id) {
        List<MainAdquisitionModel> mainAdquisitions = mainAdquisitionService.getMainAdquisitionsByUser(id);
        List<MainAdquisitionCompleteDTO> mainAdquisitionsDTO = mapMainAdquisitionModelToDTOList(mainAdquisitions);
        return new ResponseEntity<>(mainAdquisitionsDTO, HttpStatus.OK);
    }

    // SOLUCIÓN A PREGUNTA 1
    @GetMapping("/sales/{userId}")
    public ResponseEntity<List<MainAdquisitionCompleteDTO>> getSalesByUser(@PathVariable int userId) {
        List<MainAdquisitionCompleteDTO> sales = mainAdquisitionService.findSalesByUserAndOrderByDate(userId);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }
    // SOLUCIÓN A PREGUNTA 2
    @GetMapping("/day/{date}")
    public ResponseEntity<List<MainAdquisitionCompleteDTO>> showMainAdquisitionDay (@PathVariable String date) {
        List<MainAdquisitionCompleteDTO> mainAdquisitions = mainAdquisitionService.listMainAdquisitionDay(date);
        return new ResponseEntity<>(mainAdquisitions, HttpStatus.OK);
    }
}
