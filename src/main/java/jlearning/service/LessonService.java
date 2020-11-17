package jlearning.service;

import java.util.List;

import jlearning.bean.GramInfo;
import jlearning.bean.LessonInfo;
import jlearning.bean.ListenInfo;
import jlearning.bean.VocabInfo;
import jlearning.model.Lesson;

public interface LessonService extends BaseService<Integer,Lesson> {
	 List<Lesson> loadAllLessons() ;
	 void createVocab(VocabInfo vocab,int lessonId);
	 void createGram(GramInfo vocab,int lessonId);
	 void createListen(ListenInfo vocab,int lessonId);
	 Lesson createLessonWithListContent(LessonInfo lesson, int courseId);
	 boolean deleteGram(int id);
}
