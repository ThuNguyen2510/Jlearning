package jlearning.service;

import java.util.List;

import jlearning.bean.QuestionInfo;
import jlearning.bean.TestInfo;
import jlearning.model.Question;
import jlearning.model.Test;
import jlearning.model.Test.Type;

public interface TestService extends BaseService<Integer,Test>{

	Test findByType(Type type);
	boolean checkTestFinalOfLesson(int testId);
	Test findAndLoad(int id);
	List<Test> findByLevel(int level);
	List<Test> loadAllTest();
	boolean createQuestion(QuestionInfo ques, int testId);
	void createTest(List<QuestionInfo> quesList,TestInfo testInfo);
	void saveOrUpdate_(TestInfo test);
	boolean deleteQuestion(int id2);
	Question getQuestion(int id2);
}
