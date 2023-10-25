package com.kepler_apiweb.keplerapi.controller;

import com.apiweb.exception.CamposInvalidosException;
import com.kepler_apiweb.keplerapi.exception.RecursoNoEncontradoException;
import com.kepler_apiweb.keplerapi.model.UserModel;
import com.kepler_apiweb.keplerapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kepler/users")
public class UserController {
    @Autowired
    IUserService userService;

    @PostMapping("/insert")
    public ResponseEntity<String> createUser(@RequestBody UserModel user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
    }

    @GetMapping("/listUsers")
    public ResponseEntity<List<UserModel>> showUsers() {
        List<UserModel> users = userService.listUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<UserModel> searchUserById(@PathVariable String id) {
        UserModel user = userService.getUserById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Error! no se encontro el usuario con el id " + id));
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUSerById(@PathVariable String id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>("El usuario con el id: " + id + " fue eliminado exitosamente", HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUserById(@PathVariable String id, @RequestBody UserModel detailsUser) {
        UserModel user = userService.getUserById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Error! no se encontro el usuario con el id " + id));
        String updateFirstName = detailsUser.getFirst_name();
        String updateEmail = detailsUser.getEmail();
        if (updateFirstName != null && !updateFirstName.isEmpty() && updateEmail != null && !updateEmail.isEmpty()) {
            user.setFirst_name(updateFirstName);
            user.setEmail(updateEmail);

            userService.updateUser(user);
            return new ResponseEntity<>("El usuario con el ID: " + id + " fue actualizado exitosamente", HttpStatus.OK);
        } else {
            throw new CamposInvalidosException("Error! El nombre o el correo no pueden estar vacíos");
        }
    }
}
