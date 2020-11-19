package jlearning.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

import jlearning.model.Grammar;
import jlearning.service.GrammarService;

public class GrammarServiceImpl extends BaseServiceImpl implements GrammarService {

	private static final Logger logger = Logger.getLogger(GrammarServiceImpl.class);

	@Override
	public Grammar findById(Serializable key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Grammar saveOrUpdate(Grammar entity) {

		return getGrammarDAO().saveOrUpdate(entity);
	}

	@Override
	public boolean delete(Grammar entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Grammar> listGramr(int lessonId) {
		// TODO Auto-generated method stub
		return null;
	}

}
