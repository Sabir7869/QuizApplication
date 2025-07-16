package com.sabir.quizApp.dao;

import com.sabir.quizApp.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDAO extends JpaRepository<Question, Integer> {

    // ✅ Now case-insensitive query for category
    List<Question> findByCategoryIgnoreCase(String category);

    // ✅ Optional: native query also made case-insensitive
    @Query(value = "SELECT * FROM question q WHERE LOWER(q.category) = LOWER(?1) ORDER BY RANDOM() LIMIT ?2", nativeQuery = true)
    List<Question> findRandomQuestionByCategory(String category, int numQ);

}
