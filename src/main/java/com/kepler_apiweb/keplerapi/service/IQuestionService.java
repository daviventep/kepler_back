package com.kepler_apiweb.keplerapi.service;

import com.kepler_apiweb.keplerapi.model.QuestionModel;

import java.util.List;

public interface IQuestionService {
    List<QuestionModel> getAllQuestions();

    QuestionModel getQuestionById(String id);

    QuestionModel createQuestion(QuestionModel question);

    QuestionModel updateQuestion(String id, QuestionModel updatedQuestion);

    boolean deleteQuestion(String id);
}
