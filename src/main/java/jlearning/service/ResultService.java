package jlearning.service;

import java.util.List;

import jlearning.model.Result;

public interface ResultService extends BaseService<Integer, Result> {
	Result create(Result entity);
	List<Result> rankingByTest(int testId);
	List<Result> loadAllResult();
}
