package jlearning.service;

import jlearning.model.Result;

public interface ResultService extends BaseService<Integer, Result> {
	Result create(Result entity);
}
