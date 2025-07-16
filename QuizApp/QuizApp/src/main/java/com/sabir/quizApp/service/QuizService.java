package com.sabir.quizApp.service;

import com.sabir.quizApp.dao.QuestionDAO;
import com.sabir.quizApp.dao.QuizDao;
import com.sabir.quizApp.model.Question;
import com.sabir.quizApp.model.QuestionWrapper;
import com.sabir.quizApp.model.Quiz;
import com.sabir.quizApp.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDAO questionDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Question> questionList = questionDao.findRandomQuestionByCategory(category,numQ);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questionList);
        quizDao.save(quiz);
        return new ResponseEntity<>("Quiz created", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestion(Integer id) {
        Optional<Quiz> Quiz = quizDao.findById(id);
        List<Question> questionListFromDB = Quiz.get().getQuestions();
        List<QuestionWrapper> questionWrapperList = new ArrayList<>();
        for (Question question : questionListFromDB) {
            QuestionWrapper questionWrapper = new QuestionWrapper(question.getId(),question.getQuestionTitle(),question.getOptionA()
            ,question.getOptionB(),question.getOptionC(),question.getOptionD());
            questionWrapperList.add(questionWrapper);
        }
        return   new ResponseEntity<>(questionWrapperList, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        Quiz quiz = quizDao.findById(id).get();
        List<Question> questionList = quiz.getQuestions();
        int correctAnswer = 0;
        int idx = 0;
        for (Response response : responses) {
            if(response.getResponse().equals(questionList.get(idx).getAnswer())){
                correctAnswer++;
            }
            idx++;
        }
        return new ResponseEntity<>(correctAnswer, HttpStatus.OK);
    }
}
