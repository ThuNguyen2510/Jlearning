package jlearning.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import jlearning.model.Answer;
import jlearning.model.Question;
import jlearning.service.QuestionService;

public class QuestionServiceImpl extends BaseServiceImpl implements QuestionService {

	private static final Logger logger = Logger.getLogger(QuestionServiceImpl.class);

	@Override
	public Question findById(Serializable key) {
		try {
			Question q=getQuestionDAO().findById(key);
			List<Answer> ans = q.getAnswers();
			ans.size();
			return q;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
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
		Question question = getQuestionDAO().findById(questionId);
		for (Answer ans : question.getAnswers()) {
			if (ans.getIsTrue() == 1) {
				return ans.getId();
			}
		}
		return -1;
	}

	@Override
	public String findContentOfAnswerCorrect(int questionId) {
		Question question = getQuestionDAO().findById(questionId);
		for (Answer ans : question.getAnswers()) {
			if (ans.getIsTrue() == 1) {
				return ans.getContent();
			}
		}
		return "";
	}

}
