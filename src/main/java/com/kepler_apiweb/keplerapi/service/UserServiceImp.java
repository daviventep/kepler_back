package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.model.ProductModel;
import com.kepler_apiweb.keplerapi.model.UserModel;
import com.kepler_apiweb.keplerapi.repository.ICategoryRepository;
import com.kepler_apiweb.keplerapi.repository.IProductRepository;
import com.kepler_apiweb.keplerapi.repository.IUserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements IUserService {
    @Autowired
    IUserRepository userRepository;
    // Crear usuario
    @Override
    public String saveUser(UserModel user) {
        userRepository.save(user);
        return String.format("El usuario %s %s ha sido creado con el Id %s.", user.getFirst_name(), user.getLast_name(), user.get_id());
    }
    // Listar usuarios
    @Override
    public List<UserModel> listUsers() {
         List<UserModel> users = userRepository.findAll();
         return users;
    }
    // Filtrar un usuario por Id
    @Override
    public Optional<UserModel> getUserById(String userId) {
        return userRepository.findById(userId);
    }
    // Actualizar producto por Id
    @Override
    public String updateUser(UserModel user) {
        userRepository.save(user);
        return String.format("El usuario %s %s con Id %s se ha actualizado de forma exisosa.", user.getFirst_name(), user.getLast_name(), user.get_id());
    }
}
