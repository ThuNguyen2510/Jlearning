package jlearning.web.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jlearning.bean.UserInfo;
import jlearning.model.Course;
import jlearning.service.CourseService;

@Controller(value = "course")
public class CourseController extends BaseController {
	private static final Logger logger = Logger.getLogger(CourseController.class);
	@Autowired
	private CourseService courseService;
	
	@RequestMapping("/courses")
	public String index(Model model) {
		checkObjectUser(model);
		List<Course> courses = courseService.loadCourses();
		model.addAttribute("courses",courses);
		List<Course> latestCourses = courseService.LatestCourses().subList(0, 3);
		model.addAttribute("latestCourses",latestCourses);
		return "views/web/course/index";
	}
	
	@RequestMapping("/courses/{id}")
	public String course(Model model,@PathVariable("id") int id) {
		checkObjectUser(model);
		Course course = courseService.findById(id);
		model.addAttribute("course",course);
		List<Course> courses = courseService.loadCourses().subList(0, 3);
		model.addAttribute("courses",courses);
		List<Course> latestCourses = courseService.LatestCourses().subList(0, 3);
		model.addAttribute("latestCourses",latestCourses);
		return "views/web/course/course";
	}
}