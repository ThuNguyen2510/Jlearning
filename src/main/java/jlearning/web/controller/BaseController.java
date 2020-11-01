package jlearning.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import jlearning.model.Result;
import jlearning.model.User;
import jlearning.service.UserService;
import jlearning.bean.UserInfo;

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
	
	protected void checkObjectUser(Model model)
	{
		if (model.containsAttribute("user") == false)
			model.addAttribute("user", new UserInfo());
	}
	
	protected Result checkResultHasTestInLesson(User user, int lessonId) {
		Result result = null;
		for (Result rs : user.getResults()) {

			if (rs.getTest() != null) {
				if (rs.getTest().getLesson() != null) {
					if (rs.getTest().getLesson().getId() == lessonId) {
						result = rs;

						break;
					}
				}

			}

		}
		return result;
	}
}
