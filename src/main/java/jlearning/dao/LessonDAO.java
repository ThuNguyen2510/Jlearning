package jlearning.dao;

import java.util.List;

import jlearning.model.Lesson;

public interface LessonDAO extends BaseDAO<Integer,Lesson> {

	List<Lesson> loadAllLessons();
}
