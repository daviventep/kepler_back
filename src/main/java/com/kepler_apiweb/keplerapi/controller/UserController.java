package com.kepler_apiweb.keplerapi.controller;

import com.kepler_apiweb.keplerapi.exception.InvalidFieldsException;
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
@RequestMapping("/kepler/users")
public class UserController {
    @Autowired
    IUserService userService;

    @PostMapping("/")
    public ResponseEntity<String> createUser(@RequestBody UserModel user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserModel>> showUsers() {
        List<UserModel> users = userService.listUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<UserModel> searchUserById(@PathVariable int id) {
        UserModel user = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Error! no se encontro el usuario con el id " + id));
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUSerById(@PathVariable int id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>("El usuario con el id: " + id + " fue eliminado exitosamente", HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUserById(@PathVariable int id, @RequestBody UserModel detailsUser) {
        UserModel user = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Error! no se encontro el usuario con el id " + id));
        String updateFirstName = detailsUser.getFirst_name();
        String updateEmail = detailsUser.getEmail();
        String updatePhone = detailsUser.getPhone_number();
        String updateAdress = detailsUser.getAddress();
        int updatePoints = detailsUser.getPoints();
        double updateSalary = detailsUser.getSalary();
        String updatePasswd = detailsUser.getPassword();
        Date updateBirth_Date = detailsUser.getBirth_date();
        if (updateFirstName != null && !updateFirstName.isEmpty() && updateEmail != null && !updateEmail.isEmpty()) {
            user.setFirst_name(updateFirstName);
            user.setEmail(updateEmail);
            user.setPhone_number(updatePhone);
            user.setAddress(updateAdress);
            user.setPoints(updatePoints);
            user.setSalary(updateSalary);
            user.setPassword(updatePasswd);
            user.setBirth_date(updateBirth_Date);
            userService.updateUser(user);
            return new ResponseEntity<>("El usuario con el ID: " + id + " fue actualizado exitosamente", HttpStatus.OK);
        } else {
            throw new InvalidFieldsException("Error! El nombre o el correo no pueden estar vacíos");
        }
    }
}
