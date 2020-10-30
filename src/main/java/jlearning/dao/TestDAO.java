package jlearning.dao;

import jlearning.model.Test;
import jlearning.model.Test.Type;

public interface TestDAO extends BaseDAO<Integer,Test> {
	
	Test findByType(Type type);
	

}
