package jlearning.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(value = "lesson")
public class LessonController extends BaseController{
	@RequestMapping("/courses/{id1}/lessons/{id2}")
	public String index(@PathVariable("id1") int courseId,@PathVariable("id2") int lessonId,Model model) {
		checkObjectUser(model);
		
		return "views/web/lesson/index";
	}
}