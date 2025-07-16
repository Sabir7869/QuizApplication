package com.sabir.quizApp.service;

import com.sabir.quizApp.model.Question;
import com.sabir.quizApp.dao.QuestionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionDAO questionDAO;

    public ResponseEntity<List<Question>> getAllQuestion() {
        try {
            return new ResponseEntity<>(questionDAO.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Question>> getAllQuestionByCategory(String category ,int numQ) {
        try {
            List<Question> questions = questionDAO.findRandomQuestionByCategory(category,numQ);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            questionDAO.save(question);
            return new ResponseEntity<>("Question added successfully", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
    }

    public void clearAllQuestions() {
        questionDAO.deleteAll();
    }

    public int totalCount() {
        return (int) questionDAO.count();
    }

    public ResponseEntity<String> deleteById(int id) {
        try {
            questionDAO.deleteById(id);
            return new ResponseEntity<>("Question deleted successfully", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
    }

    public Optional<Question> getQuestionById(Integer id) {
        try {
            return questionDAO.findById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public ResponseEntity<String> addBulkQuestions(List<Question> questions) {
        try {
            if (questions == null || questions.isEmpty()) {
                return new ResponseEntity<>("❌ No questions to insert.", HttpStatus.BAD_REQUEST);
            }
            questionDAO.saveAll(questions);
            return new ResponseEntity<>("✅ Successfully inserted " + questions.size() + " questions.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("❌ Failed to insert questions.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
