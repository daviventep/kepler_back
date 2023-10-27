package com.kepler_apiweb.keplerapi.controller;

import com.kepler_apiweb.keplerapi.exception.InvalidFieldsException;
import com.kepler_apiweb.keplerapi.exception.ResourceExist;
import com.kepler_apiweb.keplerapi.exception.ResourceNotFoundException;
import com.kepler_apiweb.keplerapi.model.UserModel;
import com.kepler_apiweb.keplerapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/kepler/user")
public class UserController {
    @Autowired
    IUserService userService;

    @PostMapping("/")
    public ResponseEntity<String> createUser(@RequestBody UserModel user) {
        if (user.get_id() == 0) {
            throw new ResourceNotFoundException(String.format("¡Error! No se recibió un Id del usuario."));
        }
        Boolean identificationExist = userService.getUserByIdentification(user.getIdentification()).isPresent();
        if (identificationExist == true) {
            int nextIdInt = userService.getNextId();
            throw new ResourceExist(String.format("El usuario con iD %d ya existe, puedes usar el iD %d.",
                    user.get_id(),
                    nextIdInt));
        }
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserModel>> showUsers() {
        List<UserModel> users = userService.listUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserModel> searchUserById(@PathVariable int id) {
        UserModel user = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Error! no se encontro el usuario con el id " + id));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<String> deleteUSerById(@PathVariable int id, @RequestBody UserModel activityUser) {
        UserModel user = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Error! no se encontró el usuario con el ID " + id));

        boolean updatedActive = activityUser.getIs_active();
        if (updatedActive == true || updatedActive == false) {
            user.setIs_active(updatedActive);
            userService.deleteUser(user);
            return new ResponseEntity<>("El usuario con el ID: " + id + " fue marcado como inactivo exitosamente", HttpStatus.OK);
        } else {
            throw new InvalidFieldsException("Error! Los campos no pueden estar vacíos");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUserById(@PathVariable int id, @RequestBody UserModel detailsUser) {
        UserModel user = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Error! no se encontro el usuario con el id " + id));
        String updateFirstName = detailsUser.getFirst_name();
        String updateLastName = detailsUser.getLast_name();
        String updateEmail = detailsUser.getEmail();
        String updatePhone = detailsUser.getPhone_number();
        String updateAdress = detailsUser.getAddress();
        int updatePoints = detailsUser.getPoints();
        double updateSalary = detailsUser.getSalary();
        String updatePasswd = detailsUser.getPassword();
        Date update_BirthDate = detailsUser.getBirth_date();
        if (updateFirstName != null && !updateFirstName.isEmpty() &&
                updateLastName != null && !updateLastName.isEmpty() &&
                updateEmail != null && !updateEmail.isEmpty() &&
                updatePhone !=null && !updatePhone.isEmpty() &&
                updateAdress != null && !updateAdress.isEmpty() &&
                updatePoints >= 0 &&
                updateSalary >= 0 &&
                updatePasswd !=null && !updatePasswd.isEmpty() &&
                update_BirthDate != null) {
            user.setFirst_name(updateFirstName);
            user.setLast_name(updateLastName);
            user.setEmail(updateEmail);
            user.setPhone_number(updatePhone);
            user.setAddress(updateAdress);
            user.setPoints(updatePoints);
            user.setSalary(updateSalary);
            user.setPassword(updatePasswd);
            user.setBirth_date(update_BirthDate);
            userService.updateUser(user);
            return new ResponseEntity<>("El usuario con el ID: " + id + " fue actualizado exitosamente", HttpStatus.OK);
        } else {
            throw new InvalidFieldsException("Error! Los campos no pueden estar vacíos");
        }
    }
}
