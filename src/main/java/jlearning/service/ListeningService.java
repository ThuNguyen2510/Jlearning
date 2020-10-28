package jlearning.service;

import java.util.List;

import jlearning.model.Listening;

public interface ListeningService extends BaseService<Integer, Listening> {
	List<Listening> listListening(int lessonId);

}
