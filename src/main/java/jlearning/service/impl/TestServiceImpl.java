package jlearning.service.impl;

import java.io.Serializable;
import java.util.List;

import jlearning.model.Answer;
import jlearning.model.Question;
import jlearning.model.Test;
import jlearning.model.Test.Type;
import jlearning.service.TestService;

public class TestServiceImpl extends BaseServiceImpl implements TestService{

	@Override
	public Test findById(Serializable key) {
		
		return getTestDAO().findById(key);
	}

	@Override
	public Test saveOrUpdate(Test entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Test entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Test findByType(Type type) {
		Test test = getTestDAO().findByType(type);
		List<Question> questions = test.getQuestions();
		for(int i=0;i<questions.size();i++)
		{
			List<Answer> ans= questions.get(i).getAnswers();
			ans.size();
		}
		questions.size();
		return test;
	}

}
