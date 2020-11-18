package jlearning.dao;

import java.util.List;

import jlearning.model.Lesson;
import jlearning.model.Test.Type;

public interface LessonDAO extends BaseDAO<Integer,Lesson> {

	List<Lesson> loadAllLessons();
	List<Lesson> loadByType(Type type);
}
