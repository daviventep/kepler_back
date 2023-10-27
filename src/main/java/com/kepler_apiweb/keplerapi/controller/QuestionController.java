package com.kepler_apiweb.keplerapi.controller;

import com.kepler_apiweb.keplerapi.exception.ResourceExist;
import com.kepler_apiweb.keplerapi.exception.ResourceNotFoundException;
import com.kepler_apiweb.keplerapi.model.QuestionModel;
import com.kepler_apiweb.keplerapi.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/kepler/question")
public class QuestionController {
    @Autowired
    private IQuestionService questionService;

    @PostMapping("/")
    public ResponseEntity<String> createQuestion(@RequestBody QuestionModel question) {
        if (question.get_id() == 0) {
            throw new ResourceNotFoundException(String.format("¡Error! No se recibió un Id de la pregunta."));
        }
        Boolean questionExist = questionService.getQuestionById(question.get_id()).isPresent();
        if (questionExist == true) {
            int nextIdInt = questionService.getNextId();
            throw new ResourceExist(String.format("La pregunta con iD %d ya existe, puedes usar el iD %d.",
                    question.get_id(),
                    nextIdInt));
        }
        Boolean questionNameExist = questionService.getQuestionByName(question.getName()).isPresent();
        if (questionNameExist == true) {
            throw new ResourceExist(String.format("La pregunta con nombre %s ya existe.",
                    question.getName()));
        }
        question.setCreation_date(new Date());
        question.setAnswers(new ArrayList<>());
        String createdQuestion = questionService.createQuestion(question);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<QuestionModel>> getAllQuestions() {
        List<QuestionModel> questions = questionService.getAllQuestions();
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<QuestionModel> getQuestionById(@PathVariable int id) {
        QuestionModel question =
                questionService.getQuestionById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "¡Error! No se encontró la pregunta con el Id %s.", id)));;
        return ResponseEntity.ok(question);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateQuestionById(
            @PathVariable int id,
            @RequestBody QuestionModel updatedQuestion
    ) {
        String updated = questionService.updateQuestion(id, updatedQuestion);
        return new ResponseEntity<String>(updated, HttpStatus.OK);
    }
}
