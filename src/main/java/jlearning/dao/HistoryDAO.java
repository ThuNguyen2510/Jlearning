package jlearning.dao;

import java.util.List;

import jlearning.model.History;

public interface HistoryDAO extends BaseDAO<Integer,History>{
	History findByObjectId(int objectId);
}
