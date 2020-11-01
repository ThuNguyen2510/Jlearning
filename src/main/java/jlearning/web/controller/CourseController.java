package jlearning.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jlearning.bean.UserInfo;
import jlearning.model.Course;
import jlearning.model.User;
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
		model.addAttribute("courses", courses);
		List<Course> latestCourses = courseService.LatestCourses().subList(0, 3);
		model.addAttribute("latestCourses", latestCourses);
		return "views/web/course/index";
	}

	@RequestMapping("/courses/{id}")
	public String course(Model model, HttpServletRequest request, @PathVariable("id") int id) {
		checkObjectUser(model);
		Course course = courseService.findById(id);
		model.addAttribute("course", course);
		if (course.getLessons().size() == 0)
			model.addAttribute("noLesson", "Chưa có bài học");
		HttpSession session = request.getSession();
		if (session.getAttribute("currentUser") == null) // chua dang nhap
		{
			logger.info("chua dang nhap");
			model.addAttribute("chuaLogin", "Mời login để học");
		} else {
			int userId = (int) session.getAttribute("currentUser");
			User user = userService.findById(userId);
			if (user != null) {
				if (user.getLevel() == 0) {
					// lam test
					model.addAttribute("lamTestLevel",
							"Hãy làm test để biết cấp độ hiện tại. Sau đó bạn sẽ được học khóa học phù hợp!");
				} else if (user.getLevel() < course.getLevel()) {
					model.addAttribute("lowLevel", "Bạn chưa đủ cấp độ học khóa học này! Vui lòng học tiếp khóa học hiện tại");

				}
			}
		}
		List<Course> courses = courseService.loadCourses();
		model.addAttribute("courses", courses);
		List<Course> latestCourses = courseService.LatestCourses().subList(0, 3);
		model.addAttribute("latestCourses", latestCourses);
		return "views/web/course/course";
	}
}