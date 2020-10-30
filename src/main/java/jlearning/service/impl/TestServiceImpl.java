package jlearning.service.impl;

import java.io.Serializable;

import jlearning.model.Test;
import jlearning.model.Test.Type;
import jlearning.service.TestService;

public class TestServiceImpl extends BaseServiceImpl implements TestService{

	@Override
	public Test findById(Serializable key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Test saveOrUpdate(Test entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Test entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Test findByType(Type type) {
		
		return getTestDAO().findByType(type);
	}

}
