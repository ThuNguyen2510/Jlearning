package jlearning.dao;

import java.util.List;

import jlearning.model.Result;

public interface ResultDAO extends BaseDAO<Integer,Result>{
	List<Result> rankingByTest(int testId);
	List<Result> loadAllResult();
}
