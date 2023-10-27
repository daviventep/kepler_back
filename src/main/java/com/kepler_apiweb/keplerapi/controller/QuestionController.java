package com.kepler_apiweb.keplerapi.controller;

import com.kepler_apiweb.keplerapi.model.QuestionModel;
import com.kepler_apiweb.keplerapi.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kepler/question")
public class QuestionController {
    @Autowired
    private IQuestionService questionService;

    @GetMapping("/")
    public ResponseEntity<List<QuestionModel>> getAllQuestions() {
        List<QuestionModel> questions = questionService.getAllQuestions();
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionModel> getQuestionById(@PathVariable String id) {
        QuestionModel question = questionService.getQuestionById(id);
        if (question != null) {
            return new ResponseEntity<>(question, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    public ResponseEntity<QuestionModel> createQuestion(@RequestBody QuestionModel question) {
        QuestionModel createdQuestion = questionService.createQuestion(question);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionModel> updateQuestion(
            @PathVariable String id,
            @RequestBody QuestionModel updatedQuestion
    ) {
        QuestionModel updated = questionService.updateQuestion(id, updatedQuestion);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable String id) {
        boolean deleted = questionService.deleteQuestion(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
