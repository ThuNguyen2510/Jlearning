package jlearning.admin.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jlearning.model.Course;
import jlearning.model.User;
import jlearning.service.CourseService;

@Controller
@RequestMapping("/admin/courses")
public class CourseController {
	private static final Logger logger = Logger.getLogger(CourseController.class);
	
	@Autowired
	private CourseService courseService;
	
	@GetMapping(value = { "", "/" })
	public String index(Model model) {
		List<Course> courses= courseService.loadCourses();
		model.addAttribute("courses", courses);
		return "views/admin/course/courses";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable("id") int id,Model model) {
		Course course= courseService.findById(id);
		model.addAttribute("course", course);
		return "views/admin/course/course";
	}
	@GetMapping("/{id}/edit")
	public String editCourse(@PathVariable("id") int id, Model model, final RedirectAttributes redirectAttributes) {
		String typeCss = "error";
		String message = "Course not found!";
		Course course = courseService.findById(id);
		if (course != null) {
			model.addAttribute("courseForm", course);
			model.addAttribute("status", "update");
			return "views/admin/course/course-form";
		}
		redirectAttributes.addFlashAttribute("css", typeCss);
		redirectAttributes.addFlashAttribute("msg", message);
		return "redirect:/admin/courses";

	}
	
	@RequestMapping(value = "/saveupdate")
	public String saveOrUpdate(@ModelAttribute("courseForm") Course course, BindingResult result, Model model,
			final RedirectAttributes redirectAttributes) {
		
		String typeCss = "error";
		String message = "Input sai!";
		if (courseService.saveOrUpdate(course) == null) {
			redirectAttributes.addFlashAttribute("css", typeCss);
			redirectAttributes.addFlashAttribute("msg", message);
			return "redirect:/admin/courses";
		}else {
			typeCss = "success";
			message = "Sửa/Tạo khóa học thành công!!";
			redirectAttributes.addFlashAttribute("css", typeCss);
			redirectAttributes.addFlashAttribute("msg", message);
			return "redirect:/admin/courses/"+course.getId();
		}
		
	}
	@RequestMapping(value = "/add")
	public String add( Model model) {
		Course course = new Course();
		model.addAttribute("courseForm", course);
		model.addAttribute("status", "add");
		return "views/admin/course/course-form";
	}
	
	@RequestMapping(value = "/{courseId}/lessons/add")
	public String addLesson( Model model,@PathVariable("courseId") Integer id) {
		model.addAttribute("status", "add");
		return "views/admin/lesson/lesson-form";
	}
	
	@GetMapping(value = "/{id}/delete")
	public String deleteUser(@PathVariable("id") Integer id, final RedirectAttributes redirectAttributes) {
		Course course = courseService.findById(id);
		String typeCss = "error";
		String message = "User not found!";
		if (course == null) {
			redirectAttributes.addFlashAttribute("css", typeCss);
			redirectAttributes.addFlashAttribute("msg", message);
			return "redirect:/admin/courses";
		}
		typeCss = "error";
		message = "Fail to delete course!";
		if (courseService.deleteCourse(id)!=null) {
			typeCss = "success";
			message = "Course is deleted!";
		}
		redirectAttributes.addFlashAttribute("css", typeCss);
		redirectAttributes.addFlashAttribute("msg", message);
		return "redirect:/admin/courses";

	}

}
