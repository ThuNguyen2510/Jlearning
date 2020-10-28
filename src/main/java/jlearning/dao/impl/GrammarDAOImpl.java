package jlearning.dao.impl;

import java.io.Serializable;
import java.util.List;

import jlearning.dao.GenericDAO;
import jlearning.dao.GrammarDAO;
import jlearning.model.Grammar;

public class GrammarDAOImpl extends GenericDAO<Integer, Grammar> implements GrammarDAO{

	@Override
	public void delete(Grammar entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Grammar saveOrUpdate(Grammar entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Grammar findById(Serializable key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Grammar> list(int lessonId) {
		// TODO Auto-generated method stub
		return null;
	}

}
