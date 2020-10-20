package jlearning.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(value = "lesson")
public class LessonController {
	@RequestMapping("/lesson")
	public String index() {
		return "views/web/lesson/index";
	}
}