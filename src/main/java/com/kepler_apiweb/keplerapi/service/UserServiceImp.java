package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.model.UserModel;
import com.kepler_apiweb.keplerapi.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements IUserService {
    @Autowired
    IUserRepository userRepository;

    @Override
    public String saveUser(UserModel user) {
        userRepository.save(user);
        return String.format("El usuario %s %s ha sido creado con el Id %s.", user.getFirst_name(), user.getLast_name(), user.get_id());
    }

    @Override
    public List<UserModel> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserModel> getUserById(int userId) {
        return userRepository.findById(userId);
    }

    @Override
    public String updateUser(UserModel user) {
        Optional<UserModel> existingUser = userRepository.findById(user.get_id());
        if (existingUser.isPresent()) {
            UserModel updatedUser = existingUser.get();
            // Actualiza los campos necesarios
            updatedUser.setFirst_name(user.getFirst_name());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPhone_number(user.getPhone_number());
            updatedUser.setAddress(user.getAddress());
            updatedUser.setPoints(user.getPoints());
            updatedUser.setSalary(user.getSalary());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setBirth_date(user.getBirth_date());

            userRepository.save(updatedUser);  // Cambiado de updateUser a updatedUser
            return "El usuario con el ID: " + user.get_id() + " fue actualizado exitosamente";
        } else {
            return "No se encontró un usuario con el ID: " + user.get_id();
        }
    }

    @Override
    public String deleteUserById(int userId) {
        userRepository.deleteById(userId);  // Cambiado de deleteUserById a deleteById
        return "El usuario con el id: " + userId + " fue eliminado exitosamente";
    }
}
