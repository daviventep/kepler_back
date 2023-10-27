package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.DTO.ProductWithCategoryDTO;
import com.kepler_apiweb.keplerapi.exception.ResourceExist;
import com.kepler_apiweb.keplerapi.exception.ResourceNotFoundException;
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
    public String createQuestion(QuestionModel question) {
        questionRepository.save(question);
        return String.format("La pregunta %s ha sido creada.", question.getName());
    }

    @Override
    public List<QuestionModel> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Optional<QuestionModel> getQuestionById(int id) {
        return questionRepository.findById(id);
    }
    @Override
    public Optional<QuestionModel> getQuestionByName(String name) {
        return Optional.ofNullable(questionRepository.findByNameEquals(name));
    }

    @Override
    public int getNextId() {
        int return_num;
        List<QuestionModel> listQuestions = questionRepository.findLastQuestion();
        if (!listQuestions.isEmpty() && listQuestions.get(0) != null) {
            return_num = listQuestions.get(0).get_id() + 1;
        } else {
            return_num = 1;
        }
        return return_num;
    }

    @Override
    public String updateQuestion(int id, QuestionModel updatedQuestion) {
        questionRepository.save(updatedQuestion);
        return String.format("La pregunta con iD %d fue actualizada.", id);
    }



}
