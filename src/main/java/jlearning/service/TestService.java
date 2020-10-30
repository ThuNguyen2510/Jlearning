package jlearning.service;

import jlearning.model.Test;
import jlearning.model.Test.Type;

public interface TestService extends BaseService<Integer,Test>{

	Test findByType(Type type);
}
