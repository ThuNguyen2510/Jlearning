package jlearning.service;

import java.util.List;

import jlearning.model.Test;
import jlearning.model.Test.Type;

public interface TestService extends BaseService<Integer,Test>{

	Test findByType(Type type);
	boolean checkTestFinalOfLesson(int testId);
	Test findAndLoad(int id);
	List<Test> findByLevel(int level);
	List<Test> loadAllTest();
}
