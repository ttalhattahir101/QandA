package com.cgm.qanda.service;

import com.cgm.qanda.QnAApplication;
import com.cgm.qanda.dataaccessobject.QuestionRepository;
import com.cgm.qanda.dataobject.Question;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = QnAApplication.class,
        initializers = ConfigFileApplicationContextInitializer.class)
public class QuestionAnswerServiceImplTest {

    @Autowired
    QuestionAnswerService service;

    @Autowired
    QuestionRepository questionRepo;

    String defaultAnswerText;
    String questionText;
    String longQuestionText;
    String longAnswerText;

    @Before
    public void setup() {
        questionText = "Where do you live?";
        defaultAnswerText = "\"" + "the answer to life, universe and everything is 42" + "\"" + " according to" +
                "\"" + "The hitchhikers guide to the Galaxy" + "\"";
        longQuestionText =  "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a " +
                "piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, " +
                "a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin " +
                "words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in " +
                "classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 " +
                "and 1.10.33 of de Finibus Bonorum et Malorum (The Extremes of Good and Evil) by Cicero, " +
                "written in 45 BC. This book is a treatise on the theory of ethics, very popular during the " +
                "Renaissance Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a " +
                "piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock";
        longAnswerText = longQuestionText;
    }

    @Test
    public void testGetAnswers() {
        service.addQuestion(questionText, "\"In forest\"");
        List<String> answers = service.getAnswers(questionText);
        assertNotNull(answers);
        assertEquals(1, answers.size());
        assertEquals("In forest", answers.get(0));
    }

    @Test
    public void testAddQuestion() {
        service.addQuestion(questionText, "\"On land\"");
        List<String> answers = service.getAnswers(questionText);
        assertNotNull(answers);
        assertEquals(1, answers.size());
        assertEquals("On land", answers.get(0));
    }

    @Test
    public void testQuestionWithInvalidLengthShouldNotBeSaved() {
        String answerText = "Lorem Ipsum";

        service.addQuestion(longQuestionText, answerText);
        List<String> answers =  service.getAnswers(longQuestionText);
        assertEquals(0, answers.size());
    }

    @Test
    public void testAnswerWithInvalidLengthShouldNotBeSaved() {

        String answerText = "\"Near sea\" \"" + longAnswerText + "\"";

        service.addQuestion(questionText, answerText);
        List<String> answers = service.getAnswers(questionText);
        assertEquals(1, answers.size());
        assertEquals("Near sea", answers.get(0));
    }

    @Test
    public void testAnswersOrderingPersists() {
        service.addQuestion(questionText, "\"answer1\" \"answer2\" \"answer3\" \"answer4\"");
        List<String> answers = service.getAnswers(questionText);
        assertNotNull(answers);
        assertEquals("answer1", answers.get(0));
        assertEquals("answer2", answers.get(1));
        assertEquals("answer3", answers.get(2));
        assertEquals("answer4", answers.get(3));
    }

    @Test
    public void testIncorrectAnswerFormatWithMissingQuotation() {
        service.addQuestion(questionText, "\"on land\" \"in sea");
        List<String> answers = service.getAnswers(questionText);
        assertNotNull(answers);
        assertEquals(1, answers.size());
        assertEquals("on land", answers.get(0));
    }

    @Test
    public void testExistingQuestionShouldUpdateAnswers() {
        service.addQuestion(questionText, "\"In forest\" \"On Land\"");
        service.addQuestion(questionText, "\"In house\" \"In camp\"");
        List<String> answers = service.getAnswers(questionText);
        assertNotNull(answers);
        assertEquals("In house", answers.get(0));
        assertEquals("In camp", answers.get(1));
    }

    @Test
    public void testQuestionWithNullValueShouldNotBeSaved() {
        String answerText = "Null value should not saved";

        service.addQuestion(null, answerText);
        List<String> answers =  service.getAnswers(null);
        assertNotNull(answers);
        assertEquals(1, answers.size());
        assertEquals(defaultAnswerText, answers.get(0));
    }

    @Test
    public void testQuestionWithEmptyValueShouldNotBeSaved() {
        String emptyQuestionText = "";
        String answerText = "Random answer";

        service.addQuestion(emptyQuestionText, answerText);
        List<String> answers =  service.getAnswers(emptyQuestionText);
        assertNotNull(answers);
        assertEquals(1, answers.size());
        assertEquals(defaultAnswerText, answers.get(0));
    }

    @Test
    public void testAnswerWithNullValueShouldNotBeSaved() {

        service.addQuestion(questionText, null);
        List<String> answers =  service.getAnswers(questionText);
        assertNotNull(answers);
        assertEquals(1, answers.size());
        assertEquals(defaultAnswerText, answers.get(0));
    }

    @Test
    public void testAnswerWithEmptyValueShouldNotBeSaved() {
        String emptyAnswerText = "";

        service.addQuestion(questionText, emptyAnswerText);
        List<String> answers =  service.getAnswers(questionText);
        assertNotNull(answers);
        assertEquals(1, answers.size());
        assertEquals(defaultAnswerText, answers.get(0));
    }

    @After
    public void teardown()
    {
        Optional<Question> question = questionRepo.findByQuestion(questionText);
        Optional<Question> longQuestion = questionRepo.findByQuestion(longQuestionText);

        if(question.isPresent()) {
            questionRepo.delete(question.get());
        }

        if(longQuestion.isPresent()) {
            questionRepo.delete(longQuestion.get());
        }
    }

}
