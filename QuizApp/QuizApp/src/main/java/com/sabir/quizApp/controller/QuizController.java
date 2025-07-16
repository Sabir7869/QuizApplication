package com.sabir.quizApp.controller;
import com.sabir.quizApp.model.QuestionWrapper;
import com.sabir.quizApp.model.Response;
import com.sabir.quizApp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    QuizService quizService;

    @PostMapping("create")
    public ResponseEntity<String> creatQuiz(@RequestParam String category, @RequestParam int numQ,@RequestParam String title) {
        return quizService.createQuiz(category,numQ,title);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestion(@PathVariable Integer id) {
        return quizService.getQuizQuestion(id);
    }

    @PostMapping("submit/{id}")
    public ResponseEntity<Integer> submitQuiz(@PathVariable int id , @RequestBody List<Response> responses) {
        return quizService.calculateResult(id,responses);
    }
}
