let questions = [];
let currentQuestion = 0;
let userAnswers = [];
let score = 0;
let timeLeft = 20 * 60; // 20 minutes in seconds
let timerInterval;


const quizType = (localStorage.getItem("quizType") || "java").toLowerCase();
const developer = localStorage.getItem("developer") || null;

document.addEventListener('DOMContentLoaded', async () => {
    await loadQuestions();
    startTimer();
    showQuestion();
});

async function loadQuestions() {
    try {
        let apiUrl = `http://localhost:8080/question/category/${quizType}`;
        
        // If developer is specified, add it to the API call
        if (developer) {
            apiUrl = `http://localhost:8080/question/developer/${developer}`;
        }
        
        console.log(`Fetching questions from: ${apiUrl}`);
        
        const response = await fetch(apiUrl);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

        const data = await response.json();
        console.log(`Loaded ${data.length} questions for ${developer ? 'developer: ' + developer : 'category: ' + quizType}`);
        
        questions = data;

        // Each question stores multiple selected option indices
        userAnswers = new Array(questions.length).fill(null).map(() => []);

        document.getElementById('quiz-type').textContent = `${quizType.toUpperCase()} Quiz${developer ? ' by ' + developer : ''}`;
        document.getElementById('total-questions').textContent = questions.length;

        document.getElementById('loading-screen').style.display = 'none';
        document.getElementById('quiz-content').style.display = 'block';
    } catch (error) {
        console.error('Failed to load questions:', error);
        console.log('API URL attempted:', apiUrl);
        console.log('Quiz type:', quizType);
        console.log('Developer:', developer);
        alert(`Failed to load questions. Error: ${error.message}`);
    }
}

function showQuestion() {
    const question = questions[currentQuestion];
    if (!question) return;

    document.getElementById('question-number').textContent = `Question ${currentQuestion + 1} of ${questions.length}`;
    document.getElementById('question-text').textContent = question.questionTitle;

    const difficulty = document.getElementById('difficulty');
    difficulty.textContent = question.difficultyLevel;
    difficulty.className = `difficulty-badge ${question.difficultyLevel.toLowerCase()}`;

    const optionsContainer = document.getElementById('options-container');
    optionsContainer.innerHTML = '';

    const options = [
        { key: 'A', text: question.optionA },
        { key: 'B', text: question.optionB },
        { key: 'C', text: question.optionC },
        { key: 'D', text: question.optionD }
    ];

    options.forEach((option, index) => {
        const div = document.createElement('div');
        div.className = 'option';
        div.innerHTML = `
            <div class="option-letter">${option.key}</div>
            <div class="option-text">${option.text}</div>
        `;

        // Mark selected
        if (userAnswers[currentQuestion].includes(index)) {
            div.classList.add('selected');
        }

        div.addEventListener('click', () => {
            toggleSelection(index);
        });

        optionsContainer.appendChild(div);
    });
}
function startTimer() {
    const timerDisplay = document.getElementById('timer-display');

    timerInterval = setInterval(() => {
        const minutes = Math.floor(timeLeft / 60);
        const seconds = timeLeft % 60;

        timerDisplay.textContent = `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;

        if (timeLeft <= 0) {
            clearInterval(timerInterval);
            alert("⏰ Time's up! Submitting your quiz.");
            submitQuiz();
        }

        timeLeft--;
    }, 1000);
}

function submitQuiz() {
    clearInterval(timerInterval); // Stop timer
    score = 0;

    questions.forEach((q, i) => {
        const correctOptions = q.answer.split('').map(letter => {
            return { A: 0, B: 1, C: 2, D: 3 }[letter];
        });

        if (arraysEqual(correctOptions, userAnswers[i])) {
            score++;
        }
    });

    showResults();
}


function toggleSelection(index) {
    const selectedOptions = userAnswers[currentQuestion];

    if (selectedOptions.includes(index)) {
        // Deselect if already selected
        userAnswers[currentQuestion] = selectedOptions.filter(i => i !== index);
    } else {
        // Select new option
        userAnswers[currentQuestion].push(index);
    }

    showQuestion();
}

function nextQuestion() {
    if (currentQuestion < questions.length - 1) {
        currentQuestion++;
        showQuestion();
    }
}

function arraysEqual(arr1, arr2) {
    return arr1.sort().join(',') === arr2.sort().join(',');
}

function submitQuiz() {
    score = 0;

    questions.forEach((q, i) => {
        const correctOptions = q.answer.split('').map(letter => {
            return { A: 0, B: 1, C: 2, D: 3 }[letter];
        });

        if (arraysEqual(correctOptions, userAnswers[i])) {
            score++;
        }
    });

    showResults();
}

function showResults() {
    const percent = Math.round((score / questions.length) * 100);

    document.getElementById('quiz-content').style.display = 'none';
    document.getElementById('results-screen').style.display = 'block';

    document.getElementById('final-percentage').textContent = `${percent}%`;
    document.getElementById('correct-answers').textContent = score;
    document.getElementById('incorrect-answers').textContent = questions.length - score;

    document.querySelector('.score-circle').style.setProperty('--score-percentage', `${percent}%`);

    let grade = 'F';
    if (percent >= 90) grade = 'A+';
    else if (percent >= 80) grade = 'A';
    else if (percent >= 70) grade = 'B';
    else if (percent >= 60) grade = 'C';
    else if (percent >= 50) grade = 'D';

    document.getElementById('final-grade').textContent = grade;
}

function retakeQuiz() {
    currentQuestion = 0;
    userAnswers = new Array(questions.length).fill(null).map(() => []);
    score = 0;
    document.getElementById('quiz-content').style.display = 'block';
    document.getElementById('results-screen').style.display = 'none';
    showQuestion();
}

function goHome() {
    window.location.href = 'index.html';
}

// Event bindings
document.getElementById('next-btn').addEventListener('click', nextQuestion);
document.getElementById('submit-btn').addEventListener('click', submitQuiz);
document.getElementById('retake-btn').addEventListener('click', retakeQuiz);
document.getElementById('home-btn').addEventListener('click', goHome);
document.getElementById('exit-btn').addEventListener('click', goHome);
