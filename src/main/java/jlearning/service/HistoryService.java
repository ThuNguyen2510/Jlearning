package jlearning.service;

import java.util.List;

import jlearning.model.History;

public interface HistoryService extends BaseService<Integer,History> {
	 History updateTime(History his);
}
