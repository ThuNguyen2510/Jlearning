package jlearning.service;

import java.util.List;

import jlearning.bean.LessonInfo;
import jlearning.bean.VocabInfo;
import jlearning.model.Lesson;

public interface LessonService extends BaseService<Integer,Lesson> {
	 List<Lesson> loadAllLessons() ;
	 void createVocab(VocabInfo vocab,int lessonId);
	 Lesson createLessonWithListContent(LessonInfo lesson, int courseId);
}
