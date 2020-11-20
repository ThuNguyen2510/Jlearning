package jlearning.service.impl;

import java.io.Serializable;
import java.util.List;

import jlearning.model.Listening;
import jlearning.service.ListeningService;

public class ListeningServiceImpl  extends BaseServiceImpl implements ListeningService{

	@Override
	public Listening findById(Serializable key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Listening saveOrUpdate(Listening entity) {
		
		return getListeningDAO().saveOrUpdate(entity);
	}

	@Override
	public boolean delete(Listening entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Listening> listListening(int lessonId) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
