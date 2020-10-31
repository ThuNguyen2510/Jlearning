package jlearning.service.impl;

import java.io.Serializable;

import org.apache.log4j.Logger;

import jlearning.model.Result;
import jlearning.service.ResultService;

public class ResultServiceImpl  extends BaseServiceImpl implements ResultService {

	private static final Logger logger = Logger.getLogger(ResultServiceImpl.class);
	@Override
	public Result findById(Serializable key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result saveOrUpdate(Result entity) {
		try {
			return getResultDAO().saveOrUpdate(entity);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public boolean delete(Result entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Result create(Result entity) {
		return getResultDAO().saveOrUpdate(entity);
	}

}
