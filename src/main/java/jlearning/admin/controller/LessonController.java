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

import jlearning.model.Alphabet;
import jlearning.model.Course;
import jlearning.model.Lesson;
import jlearning.service.AlphabetService;
import jlearning.service.CourseService;
import jlearning.service.LessonService;

@Controller
@RequestMapping("/admin/lessons")
public class LessonController {
	private static final Logger logger = Logger.getLogger(LessonController.class);

	@Autowired
	private LessonService lessonService;

	@Autowired
	private AlphabetService alphabetService;
	
	@GetMapping(value = { "", "/" })
	public String index(Model model) {
		List<Lesson> lessons = lessonService.loadAllLessons();
		model.addAttribute("lessons", lessons);
		return "views/admin/lesson/lessons";
	}

	@GetMapping(value = "/{id}")
	public String show(Model model, @PathVariable("id") int id) {
		Lesson lesson = lessonService.findById(id);
		if(id<3) {
			model.addAttribute("alphabet","alphabet");
			Alphabet alphabet = alphabetService.findById(id);
			model.addAttribute("alphabet", alphabet);
		}
		model.addAttribute("lesson", lesson);
		return "views/admin/lesson/lesson";
	}
	
	
	@GetMapping("/{id}/edit")
	public String editCourse(@PathVariable("id") int id, Model model, final RedirectAttributes redirectAttributes) {
		String typeCss = "error";
		String message = "Lesson not found!";
		Lesson lesson = lessonService.findById(id);
		if (lesson != null) {
			model.addAttribute("lessonForm", lesson);
			model.addAttribute("status", "update");
			return "views/admin/lesson/lesson-form";
		}
		redirectAttributes.addFlashAttribute("css", typeCss);
		redirectAttributes.addFlashAttribute("msg", message);
		return "redirect:/admin/lessons";

	}
	@RequestMapping(value = "/add")
	public String add( Model model) {
		Lesson course = new Lesson();
		model.addAttribute("lessonForm", course);
		model.addAttribute("status", "add");
		return "views/admin/lesson/lesson-form";
	}
	@RequestMapping(value = "/addNormal")
	public String addManual( Model model) {
		Lesson course = new Lesson();
		model.addAttribute("lessonForm", course);
		model.addAttribute("status", "add");
		return "views/admin/lesson/newLessonManual";
	}
	@RequestMapping(value = "/addImport")
	public String addImport( Model model) {
		Lesson course = new Lesson();
		model.addAttribute("lessonForm", course);
		model.addAttribute("status", "add");
		return "views/admin/lesson/newLessonImport";
	}
	
	@RequestMapping(value = "/update")
	public String saveOrUpdate(@ModelAttribute("lessonForm") Lesson lesson, BindingResult result, Model model,
			final RedirectAttributes redirectAttributes) {
		
		String typeCss = "error";
		String message = "Input sai!";
		Lesson old= lessonService.findById(lesson.getId());
		lesson.setCourse(old.getCourse());
		if (lessonService.saveOrUpdate(lesson) == null) {
			redirectAttributes.addFlashAttribute("css", typeCss);
			redirectAttributes.addFlashAttribute("msg", message);
			return "redirect:/admin/lessons";
		}else {
			typeCss = "success";
			message = "Sửa/Tạo khóa học thành công!!";
			redirectAttributes.addFlashAttribute("css", typeCss);
			redirectAttributes.addFlashAttribute("msg", message);
			return "redirect:/admin/lessons/"+lesson.getId();
		}
		
	}

}