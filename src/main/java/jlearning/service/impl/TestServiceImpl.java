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
		try {
			Test entity_ = findAndLoad(entity.getId());
			for (int i = 0; i < entity_.getQuestions().size(); i++) {
				int q = entity_.getQuestions().get(i).getId();
				deleteQuestion(q);

			}
			getTestDAO().delete(entity_);

			return true;
		} catch (

		Exception ecx) {
			return false;
		}

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
			q.setLevel(ques.getLevel());
			if(ques.getPart()==1) {
				q.setPart(Part.vocab);
			}
			if(ques.getPart()==2) {
				q.setPart(Part.gram);
			}
			if(ques.getPart()==3) {
				q.setPart(Part.listen);
			}
			if(ques.getPart()==4) {
				q.setPart(Part.read);
			}
			q.setScore(1);
			Test test = getTestDAO().findById(testId);
			q.setTest(test);
			int qu=test.getQuestionQuantity();
			test.setQuestionQuantity(qu++);
			getQuestionDAO().saveOrUpdate(q);
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
		test.setQuestionQuantity(quesList.size());
		getTestDAO().saveOrUpdate(test);

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

	@Override
	public boolean deleteQuestion(int id2) {
		try {
			Question q = getQuestionDAO().findById(id2);
			for (int j = 0; j < q.getAnswers().size(); j++) {
				getAnswerDAO().delete(q.getAnswers().get(j));
			}
			getQuestionDAO().delete(q);
			return true;

		} catch (Exception ex) {

			return false;
		}

	}

	@Override
	public Question getQuestion(int id2) {
		Question q= getQuestionDAO().findById(id2);
		List<Answer> ans= q.getAnswers();
		ans.size();
		return q;
	}

}
