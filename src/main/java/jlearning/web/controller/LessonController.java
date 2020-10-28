package jlearning.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jlearning.model.Lesson;
import jlearning.service.LessonService;

@Controller(value = "lesson")
public class LessonController extends BaseController{
	private static final Logger logger = Logger.getLogger(LessonController.class);
	@Autowired
	private LessonService lessonService;
	
	@RequestMapping("/courses/{id1}/lessons/{id2}")
	public String index(@PathVariable("id1") int courseId,@PathVariable("id2") int lessonId,Model model) {
		checkObjectUser(model);
		Lesson lesson = lessonService.findById(lessonId);
		return "views/web/lesson/index";
	}
}