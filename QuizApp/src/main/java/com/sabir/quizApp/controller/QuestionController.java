package com.sabir.quizApp.controller;

import com.sabir.quizApp.model.Question;
import com.sabir.quizApp.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/all") // Renamed for better readability
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestion().getBody();
    }

    @GetMapping("/category/{category}")
    public List<Question> getAllQuestionsByCategory(@PathVariable String category) {
        return questionService.getAllQuestionByCategory(category,15).getBody();
    }
    @PostMapping("/add")
    public ResponseEntity<String> addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);
    }

    @GetMapping("/totalCount")
    public int totalCount() {
        return questionService.totalCount();
    }

    @GetMapping("/{id}")
    public Optional<Question> getQuestionById(@PathVariable Integer id) {
        return questionService.getQuestionById(id);
    }
    @PostMapping("/bulk-upload")
    public ResponseEntity<String> addBulkQuestions(@RequestBody List<Question> questions) {
        return questionService.addBulkQuestions(questions);
    }

}