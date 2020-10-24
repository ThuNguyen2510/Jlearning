package jlearning.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(value = "lesson")
public class LessonController extends BaseController{
	@RequestMapping("/lesson")
	public String index(Model model) {
		checkObjectUser(model);
		return "views/web/lesson/index";
	}
}