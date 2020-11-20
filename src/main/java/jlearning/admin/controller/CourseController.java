package jlearning.admin.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jlearning.bean.LessonInfo;
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
		List<Course> courses = courseService.loadCourses();
		model.addAttribute("courses", courses);
		return "views/admin/course/courses";
	}

	@GetMapping("/{id}")
	public String show(@PathVariable("id") int id, Model model) {
		Course course = courseService.findById(id);
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
			final RedirectAttributes redirectAttributes, HttpServletRequest request,
			@RequestParam(value = "fileLogo", required = false) MultipartFile fileLogo,
			@RequestParam(value = "fileImage", required = false) MultipartFile fileImage)
			throws IllegalStateException, IOException {
		if (fileLogo.isEmpty() != true) {
			File fileNew = new File("C:/Users/nguye/eclipse-workspace/Jlearning/src/main/webapp/assets/upload",
					fileLogo.getOriginalFilename());
			fileLogo.transferTo(fileNew);
			course.setLogo("/haru/assets/upload/" + fileLogo.getOriginalFilename());
		}
		if (fileImage.isEmpty() != true) {
			File fileNew = new File("C:/Users/nguye/eclipse-workspace/Jlearning/src/main/webapp/assets/upload",
					fileImage.getOriginalFilename());
			fileImage.transferTo(fileNew);
			course.setImage("/haru/assets/upload/" + fileImage.getOriginalFilename());
		}
		String typeCss = "error";
		String message = "Input sai!";
		HttpSession session = request.getSession();
		if (session.getAttribute("newLesson") != null) {
			List<LessonInfo> lessons = (List<LessonInfo>) session.getAttribute("newLesson");
			courseService.create(lessons, course);
			typeCss = "success";
			message = "Tạo khóa học thành công!!";
			session.removeAttribute("newLesson");
			session.removeAttribute("vocabs");
			session.removeAttribute("grams");
			session.removeAttribute("listens");
			session.removeAttribute("courseId");
			session.removeAttribute("newCourse");
		}

		else {
			typeCss = "success";
			message = "Tạo/Sửa khóa học thành công!!";
			courseService.saveOrUpdate(course);
			session.removeAttribute("newLesson");
			session.removeAttribute("vocabs");
			session.removeAttribute("grams");
			session.removeAttribute("listens");
			session.removeAttribute("courseId");
			session.removeAttribute("newCourse");
			redirectAttributes.addFlashAttribute("css", typeCss);
			redirectAttributes.addFlashAttribute("msg", message);

		}
		return "redirect:/admin/courses/" + course.getId();
	}

	@RequestMapping(value = "/add")
	public String add(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Course course = new Course();
		model.addAttribute("courseForm", course);
		if (session.getAttribute("newLesson") != "") {
			List<LessonInfo> lessons = (List<LessonInfo>) session.getAttribute("newLesson");
			model.addAttribute("newLesson", lessons);
		}
		session.setAttribute("newCourse", "0");
		model.addAttribute("status", "add");

		return "views/admin/course/course-form";
	}

	@RequestMapping(value = "/{courseId}/lessons/add")
	public String addLesson(Model model, @PathVariable("courseId") Integer id, HttpServletRequest request) {
		HttpSession session = request.getSession();
		model.addAttribute("status", "add");
		session.setAttribute("courseId", id);

		return "views/admin/lesson/lesson-form";
	}

	@GetMapping(value = "/{id}/delete")
	public String deleteUser(@PathVariable("id") Integer id, final RedirectAttributes redirectAttributes) {
		Course course = courseService.findById(id);
		String typeCss = "";
		String message = "";
		if (course == null) {
			redirectAttributes.addFlashAttribute("css", typeCss);
			redirectAttributes.addFlashAttribute("msg", message);
			return "redirect:/admin/courses";
		}
		typeCss = "error";
		message = "Xóa khóa học thất bại!";
		if (courseService.delete(course) != false) {
			typeCss = "success";
			message = "Xóa khóa học thành công!";
		}
		redirectAttributes.addFlashAttribute("css", typeCss);
		redirectAttributes.addFlashAttribute("msg", message);
		return "redirect:/admin/courses";

	}

}
