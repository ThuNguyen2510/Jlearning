package jlearning.service;

import java.util.List;

import jlearning.model.Lesson;

public interface LessonService extends BaseService<Integer,Lesson> {
	 List<Lesson> loadAllLessons() ;

}
