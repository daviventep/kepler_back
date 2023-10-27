package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.DTO.ProductWithCategoryDTO;
import com.kepler_apiweb.keplerapi.model.ProductModel;
import com.kepler_apiweb.keplerapi.model.QuestionModel;
import com.kepler_apiweb.keplerapi.repository.ICategoryRepository;
import com.kepler_apiweb.keplerapi.repository.IProductRepository;
import com.kepler_apiweb.keplerapi.repository.IQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImp implements IQuestionService {
    @Autowired
    IQuestionRepository questionRepository;
    @Autowired
    IProductRepository productRepository;
    @Autowired
    private MongoTemplate
            mongoTemplate;

    @Override
    public List<QuestionModel> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public QuestionModel getQuestionById(String id) {
        return null;
    }

    @Override
    public int getNextId() {
        int return_num;
        List<ProductModel> listProducts = productRepository.findLastProduct();
        System.out.println(listProducts);
        if (!listProducts.isEmpty() && listProducts.get(0) != null) {
            return_num = listProducts.get(0).get_id() + 1;
        } else {
            return_num = 1;
        }
        return return_num;
    }

    @Override
    public QuestionModel createQuestion(QuestionModel question) {
        return null;
    }

    @Override
    public QuestionModel updateQuestion(String id, QuestionModel updatedQuestion) {
        return null;
    }

    @Override
    public boolean deleteQuestion(String id) {
        return false;
    }


}
