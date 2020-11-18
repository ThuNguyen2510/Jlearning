package jlearning.dao;

import java.util.List;

import jlearning.model.Lesson;
import jlearning.model.Test;
import jlearning.model.Test.Type;

public interface TestDAO extends BaseDAO<Integer, Test> {

	Test findByType(Type type);

	List<Test> findByLevel(int level);

	List<Test> loadAllTest();

	
}
