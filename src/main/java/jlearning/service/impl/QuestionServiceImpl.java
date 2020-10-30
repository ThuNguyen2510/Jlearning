package jlearning.service.impl;

import java.io.Serializable;

import jlearning.model.Question;
import jlearning.service.QuestionService;

public class QuestionServiceImpl extends BaseServiceImpl implements QuestionService {

	@Override
	public Question findById(Serializable key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Question saveOrUpdate(Question entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Question entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int findAnswerIdCorrect(int questionId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String findContentOfAnswerCorrect(int questionId) {
		// TODO Auto-generated method stub
		return null;
	}

}
