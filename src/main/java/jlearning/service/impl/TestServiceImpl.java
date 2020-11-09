package jlearning.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import jlearning.model.Answer;
import jlearning.model.Lesson;
import jlearning.model.Question;
import jlearning.model.Test;
import jlearning.model.Test.Type;
import jlearning.service.TestService;

public class TestServiceImpl extends BaseServiceImpl implements TestService {

	private static final Logger logger = Logger.getLogger(TestServiceImpl.class);

	@Override
	public Test findById(Serializable key) {
		Test test = getTestDAO().findById(key);
		return test;
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
		for (int i = 0; i < questions.size(); i++) {
			List<Answer> ans = questions.get(i).getAnswers();
			ans.size();
		}
		questions.size();
		return test;
	}

	@Override
	public boolean checkTestFinalOfLesson(int testId) {
		Test test = getTestDAO().findById(testId);
		int lessonId = test.getLesson().getId();
		List<Lesson> list = getLessonDAO().findById(lessonId).getCourse().getLessons();
		
		logger.info("SIZE "+list.size());
		logger.info("TEST ID "+testId);
		if(list.get(list.size()-1).getTests()!=null) {
			if(testId == list.get(list.size()-1).getTests().get(0).getId()) return true;
		}
		
		return false;
	}

	@Override
	public Test findAndLoad(int id) {
		Test test = getTestDAO().findById(id);
		List<Question> ques = test.getQuestions();
		for (int i = 0; i < ques.size(); i++) {
			List<Answer> ans = ques.get(i).getAnswers();
			ans.size();
		}
		ques.size();

		return test;
	}

}
