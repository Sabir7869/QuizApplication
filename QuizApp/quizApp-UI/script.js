function startQuiz(type, developer = null) {
  localStorage.setItem("quizType", type);
  
  if (developer) {
    localStorage.setItem("developer", developer);
  } else {
    localStorage.removeItem("developer");
  }
  
  window.location.href = "quiz.html";
}

// Function to start quiz by developer
function startQuizByDeveloper(developer) {
  localStorage.setItem("developer", developer);
  localStorage.removeItem("quizType"); // Clear category filter
  window.location.href = "quiz.html";
}

// Function to start quiz by both category and developer
function startQuizByCategoryAndDeveloper(type, developer) {
  localStorage.setItem("quizType", type);
  localStorage.setItem("developer", developer);
  window.location.href = "quiz.html";
}
