package com.agilefaqs.stackoverflow.questions.servcie;

import com.agilefaqs.stackoverflow.questions.dao.QuestionsDao;
import com.agilefaqs.stackoverflow.questions.model.Question;
import com.agilefaqs.stackoverflow.questions.queues.Queues;
import com.agilefaqs.stackoverflow.questions.queues.Queues.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionsService {

    private QuestionsDao questionsDao;
    private Channel<Question> postedQuestionsChannel;


    @Autowired
    public QuestionsService(QuestionsDao questionsDao) {
        postedQuestionsChannel = Queues
        .createEmptyChannelForTopic("Posted_Questions");
        this.questionsDao = questionsDao;
    }


    public Question get(String questionId) {
        return questionsDao.get(questionId);
    }

    public String save(Question question) {
        System.out.println("Saving question");
        questionsDao.save(question);
        postedQuestionsChannel.publish(question);
        return question.getId();
    }

    public void update(String questionId, Question input) {
        input.setId(questionId);
        questionsDao.save(input);
        postedQuestionsChannel.publish(input);
    }

    public void upvote(String questionId) {
        questionsDao.get(questionId).upvote();
    }

    public void downvote(String questionId) {
        questionsDao.get(questionId).downvote();
    }
}
