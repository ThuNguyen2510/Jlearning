package jlearning.dao;

import java.util.List;

import jlearning.model.Listening;

public interface ListeningDAO extends BaseDAO<Integer,Listening> {

	List<Listening> list(int lessonId);
}
