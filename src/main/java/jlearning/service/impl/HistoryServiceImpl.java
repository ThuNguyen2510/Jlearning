package jlearning.service.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import jlearning.model.History;
import jlearning.service.HistoryService;

public class HistoryServiceImpl extends BaseServiceImpl implements HistoryService {

	private static final Logger logger = Logger.getLogger(HistoryServiceImpl.class);

	@Override
	public History findById(Serializable key) {
		
		return getHistoryDAO().findById(key);
	}

	public History updateTime(History his) {
		try {
			Date date = new Date();
			// getTime() returns current time in milliseconds
			long time = date.getTime();
			// Passed the milliseconds to constructor of Timestamp class
			Timestamp ts = new Timestamp(time);
			History h=getHistoryDAO().findById(his.getId());
			h.setCreate_time(ts);
			logger.info("TIME "+h.getCreate_time());
			return getHistoryDAO().saveOrUpdate(h);

		} catch (Exception e) {
			throw(e);

		}
	}

	@Override
	public History saveOrUpdate(History entity) {
		try {
			return getHistoryDAO().saveOrUpdate(entity);
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}

	@Override
	public boolean delete(History entity) {
		// TODO Auto-generated method stub
		return false;
	}

}
