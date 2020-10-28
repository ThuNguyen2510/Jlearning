package jlearning.service;

import java.util.List;

import jlearning.model.Grammar;

public interface GrammarService extends BaseService<Integer, Grammar> {

	List<Grammar> listGramr(int lessonId);
}
