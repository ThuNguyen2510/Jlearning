package jlearning.service;

import java.util.List;

import jlearning.model.Vocabulary;

public interface VocabularyService extends BaseService<Integer, Vocabulary> {

	List<Vocabulary> listVocab(int lessonId);
}
