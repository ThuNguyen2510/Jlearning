package jlearning.service;

import java.util.List;

import jlearning.bean.GramInfo;
import jlearning.bean.LessonInfo;
import jlearning.bean.ListenInfo;
import jlearning.bean.VocabInfo;
import jlearning.model.Grammar;
import jlearning.model.Lesson;
import jlearning.model.Listening;
import jlearning.model.Test.Type;

public interface LessonService extends BaseService<Integer,Lesson> {
	 List<Lesson> loadAllLessons() ;
	 List<Lesson> loadByType(Type type);
	 void createVocab(VocabInfo vocab,int lessonId);
	 void createGram(GramInfo vocab,int lessonId);
	 void createListen(ListenInfo vocab,int lessonId);
	 Lesson createLessonWithListContent(LessonInfo lesson, int courseId);
	 boolean deleteGram(int id);
	 boolean deleteVocab(int id2);
	 boolean deleteListen(int id2);
	 boolean editGram(Grammar gram);
	 boolean editListen(Listening lis);
	 Grammar getGram(int id);
}
