package jlearning.dao;

import java.util.List;

import jlearning.model.Vocabulary;

public interface VocabularyDAO extends BaseDAO<Integer,Vocabulary> {
	List<Vocabulary> list(int lessonId);

}
