package jlearning.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jlearning.bean.UserInfo;

@Controller(value = "course")
public class CourseController extends BaseController {
	@RequestMapping("/courses")
	public String index(Model model) {
		checkObjectUser(model);
		return "views/web/course/index";
	}
	
	@RequestMapping("/courses/1")
	public String course(Model model) {
		checkObjectUser(model);
		return "views/web/course/course";
	}
}