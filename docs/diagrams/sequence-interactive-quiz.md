# Sequence — Interactive Quiz (CLI)

How `create-interactive-quiz` generates a quiz from the prepared GCS context and then drives a question-by-question console loop against Gemini.

```mermaid
sequenceDiagram
    autonumber
    actor U as User (console)
    participant CIQ as CreateInteractiveQuizCommand
    participant J as Jarvis
    participant IQ as InteractiveQuiz<br/>(MultipleChoice / ShortAnswer)
    participant G as Gemini (VertexAI)
    participant R as ConsoleInteractiveQuizRunner

    U->>CIQ: create-interactive-quiz SHORT_ANSWER 5
    CIQ->>CIQ: validate args, parse quiz type and count
    CIQ->>J: Jarvis.getInstance()
    J->>G: new VertexAI + GenerativeModel
    J->>G: initializeMultiModalInput(bucket URIs)

    CIQ->>J: createInteractiveQuiz(type, 5)
    J->>IQ: InteractiveQuiz.getQuiz(type, gemini, 5)
    IQ->>G: respond(quiz prompt)<br/>"Generate 5 questions with all of the answers at the bottom."
    G-->>IQ: quizText (questions + answer key)
    IQ-->>J: InteractiveQuiz instance
    J-->>CIQ: quiz instance

    CIQ->>R: runQuiz(quiz)
    loop for each question in round
        R->>IQ: getNextQuestion()
        IQ->>G: textInput(getNextQuestionPrompt())
        G-->>IQ: question text
        IQ-->>R: question text
        R->>U: print question + "Enter: "
        U-->>R: typed answer
        alt answer == "0"
            R-->>U: "Quitting..."
            R-->>CIQ: return
        else normal answer
            R->>IQ: evaluateAnswer(answer)
            IQ->>G: textInput(evaluateAnswerPrompt(answer))
            G-->>IQ: evaluation text
            IQ-->>R: evaluation text
            R->>U: print evaluation
        end
    end
    Note over R,G: Outer while-loop starts another round<br/>of numberOfQuestions until user types "0"

    CIQ->>J: Jarvis.close()
    J->>G: VertexAI.close()
```

## Things worth knowing

- `InteractiveQuiz.getQuiz` is a small factory: it returns a [MultipleChoiceInteractiveQuiz](../../src/main/java/com/christophertbarrerasconsulting/studyjarvis/quiz/MultipleChoiceInteractiveQuiz.java) or [ShortAnswerInteractiveQuiz](../../src/main/java/com/christophertbarrerasconsulting/studyjarvis/quiz/ShortAnswerInteractiveQuiz.java) subclass based on the CLI argument. Both subclasses override `getQuizPrompt`, `getNextQuestionPrompt`, or `evaluateAnswerPrompt` to shape Gemini's behavior.
- The initial `respond` call adds the full quiz (questions + answer key) into `Gemini.parts`, so subsequent `getNextQuestion` / `evaluateAnswer` prompts can refer back to it through multi-modal context.
- The outer `while (true)` in `ConsoleInteractiveQuizRunner.runQuiz` means the same quiz is re-iterated in rounds until the user types `0` — it does **not** generate a new quiz per round.
- The server has no equivalent handler today; interactive quizzes are CLI-only. The server exposes only the one-shot `/secure/jarvis/create-quiz`.
