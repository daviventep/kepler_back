package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.model.QuestionModel;

import java.util.List;
import java.util.Optional;

public interface IQuestionService {
    String createQuestion(QuestionModel question);

    List<QuestionModel> getAllQuestions();
    Optional<QuestionModel> getQuestionById(int id);
    int getNextId();

    String updateQuestion(int id, QuestionModel updatedQuestion);

    Optional<QuestionModel> getQuestionByName(String name);
}