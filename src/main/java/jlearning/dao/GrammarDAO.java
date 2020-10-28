package jlearning.dao;

import java.util.List;

import jlearning.model.Grammar;

public interface GrammarDAO extends BaseDAO<Integer,Grammar> {
	List<Grammar> list(int lessonId);
}
