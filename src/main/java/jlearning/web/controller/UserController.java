package jlearning.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jlearning.service.UserService;

@Controller(value = "user")
public class UserController extends BaseController  {
	@Autowired
	private UserService userService;
	private static final Logger logger = Logger.getLogger(UserController.class);

	@RequestMapping("/users/1")
	public String index(Model model) {
		checkObjectUser(model);
		return "views/web/user/index";
	}

	@RequestMapping("/users/1/examHis")
	public String exams(Model model) {
		checkObjectUser(model);
		return "views/web/user/examHistory";
	}

	@RequestMapping("/users/1/lessonHis")
	public String lessons(Model model) {
		checkObjectUser(model);
		return "views/web/user/lessonHistory";
	}

	@RequestMapping("/users/1/blogs")
	public String blogs(Model model) {
		checkObjectUser(model);
		return "views/web/user/myBlogs";
	}
}
