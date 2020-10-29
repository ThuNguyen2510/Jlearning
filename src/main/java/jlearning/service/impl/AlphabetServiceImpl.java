package jlearning.service.impl;

import java.io.Serializable;

import org.apache.log4j.Logger;

import jlearning.model.Alphabet;
import jlearning.service.AlphabetService;

public class AlphabetServiceImpl extends BaseServiceImpl implements AlphabetService {

	private static final Logger logger = Logger.getLogger(AlphabetServiceImpl.class);

	@Override
	public Alphabet findById(Serializable key) {
		try {
			
			return getAlphabetDAO().findById(key);
		} catch (Exception e) {
			
			logger.error(e);
			throw e;

		}

	}

	@Override
	public Alphabet saveOrUpdate(Alphabet entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(Alphabet entity) {
		// TODO Auto-generated method stub
		return false;
	}

}
