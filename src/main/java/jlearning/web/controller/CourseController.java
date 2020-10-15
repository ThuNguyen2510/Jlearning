package jlearning.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(value = "course")
public class CourseController {
	@RequestMapping("/courses")
	public String index() {
		return "views/web/course/index";
	}
	
	@RequestMapping("/courses/1")
	public String course() {
		return "views/web/course/course";
	}
}