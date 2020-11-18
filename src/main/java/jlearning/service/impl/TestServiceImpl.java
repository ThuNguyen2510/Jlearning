package jlearning.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import jlearning.bean.AnswerInfo_;
import jlearning.bean.QuestionInfo;
import jlearning.model.Answer;
import jlearning.model.Lesson;
import jlearning.model.Question;
import jlearning.model.Question.Part;
import jlearning.model.Test;
import jlearning.model.Test.Type;
import jlearning.bean.TestInfo;
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

		return getTestDAO().saveOrUpdate(entity);
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

		logger.info("SIZE " + list.size());
		logger.info("TEST ID " + testId);
		if (list.get(list.size() - 1).getTests() != null) {
			if (testId == list.get(list.size() - 1).getTests().get(0).getId())
				return true;
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

	@Override
	public List<Test> findByLevel(int level) {

		return getTestDAO().findByLevel(level);
	}

	@Override
	public List<Test> loadAllTest() {

		return getTestDAO().loadAllTest();
	}

	@Override
	public boolean createQuestion(QuestionInfo ques, int testId) {
		try {
			Question q = new Question();
			q.setContent(ques.getContent());
			q.setLevel(0);
			q.setPart(Part.vocab);
			q.setScore(1);
			Test test = getTestDAO().findById(testId);
			q.setTest(test);
			getQuestionDAO().saveOrUpdate(q);
			logger.info("ANS1 " + ques.getAnsList().get(0).getContent());
			for (int i = 0; i < ques.getAnsList().size(); i++) {
				Answer a = new Answer();
				AnswerInfo_ a_ = ques.getAnsList().get(i);
				a.setContent(a_.getContent());
				a.setIsTrue(a_.getIsTrue());
				a.setQuestion(q);
				getAnswerDAO().saveOrUpdate(a);
			}

			return true;
		} catch (Exception ex) {
			return false;
		}

	}

	@Override
	public void createTest(List<QuestionInfo> quesList, TestInfo testInfo) {
		Test test = new Test();
		String type = testInfo.getType();
		if (type.compareTo("0") == 0) {
			test.setType(Type.LESSON);
			Lesson lesson = getLessonDAO().findById(testInfo.getLessonId());
			test.setLesson(lesson);
		}
		if (type.compareTo("1") == 0) {
			test.setType(Type.LEVEL);
		}
		if (type.compareTo("2") == 0) {
			test.setType(Type.EXAM);
		}
		test.setLevel(testInfo.getLevel());
		test.setTime(testInfo.getTime());
		// set Lesson
		test.setName(testInfo.getName());
		getTestDAO().saveOrUpdate(test);
		for (int i = 0; i < quesList.size(); i++) {
			createQuestion(quesList.get(i), test.getId());
		}

	}

	@Override
	public void saveOrUpdate_(TestInfo testInfo) {
		Test test = new Test();
		String type = testInfo.getType();
		if (type.compareTo("0") == 0)
			test.setType(Type.LESSON);
		if (type.compareTo("1") == 0) {
			test.setType(Type.LEVEL);
		}
		if (type.compareTo("2") == 0) {
			test.setType(Type.EXAM);
		}
		test.setLevel(testInfo.getLevel());
		test.setTime(testInfo.getTime());
		// set Lesson
		test.setName(testInfo.getName());
		getTestDAO().saveOrUpdate(test);

	}

}
