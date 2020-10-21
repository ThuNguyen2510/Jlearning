package jlearning.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import jlearning.model.User;
import jlearning.service.UserService;

public abstract class BaseController {

	@Autowired
	protected UserService userService;

	protected User loadCurrentUser(HttpServletRequest request) {

		HttpSession session = request.getSession();
		Object userId =  session.getAttribute("currentUser");
		if(userId!=null) {
			User user = userService.findById((int) userId);
			if (user != null) return user;
		}
		return null;
	}
}
