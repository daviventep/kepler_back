package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.model.ProductModel;
import com.kepler_apiweb.keplerapi.model.UserModel;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    String saveUser(UserModel user);
    List<UserModel> listUsers();
    Optional<UserModel> getUserById(String userId);
    String updateUser(UserModel user);

}
