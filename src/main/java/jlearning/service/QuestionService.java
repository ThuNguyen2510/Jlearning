package jlearning.service;

import jlearning.model.Question;

public interface QuestionService  extends BaseService<Integer,Question> {
	int findAnswerIdCorrect(int questionId);
	String findContentOfAnswerCorrect(int questionId);
}
