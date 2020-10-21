package jlearning.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jlearning.service.UserService;

@Controller(value = "user")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping("/users/1")
	public String index() {

		return "views/web/user/index";
	}

	@RequestMapping("/users/1/examHis")
	public String exams() {
		return "views/web/user/examHistory";
	}

	@RequestMapping("/users/1/lessonHis")
	public String lessons() {
		return "views/web/user/lessonHistory";
	}

	@RequestMapping("/users/1/blogs")
	public String blogs() {
		return "views/web/user/myBlogs";
	}
}
