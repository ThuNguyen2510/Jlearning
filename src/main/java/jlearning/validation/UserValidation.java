package jlearning.validation;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.apache.commons.lang3.StringUtils;
import jlearning.bean.UserInfo;
import jlearning.model.User;

public class UserValidation implements Validator {
	
	@Override
	public boolean supports(Class<?> type) {
		return User.class.isAssignableFrom(type);
	}

	@Override
	public void validate(Object object, Errors errors) {
		UserInfo user = (UserInfo) object;

		if (StringUtils.isBlank(user.getName())) {
			errors.rejectValue("name", "NotEmpty");
		}

		if (StringUtils.isBlank(user.getEmail())) {
			errors.rejectValue("email", "NotEmpty");
		} else if (new EmailValidator().isValid(user.getEmail(), null) == false) {
			errors.rejectValue("email", "Error.Email.Format");
		}
		
		if (StringUtils.isBlank(user.getPassword())) {
			errors.rejectValue("password", "NotEmpty");
		} else if (user.getPassword().length() < 5) {
			errors.rejectValue("password",  "Error.Pass.Size");
		}
		else if(!user.getPassword().equals(user.getConfirmPassword())) {
			errors.rejectValue("confirmPassword", "Error.Pass.Incorrect");
		}
		
	}

}