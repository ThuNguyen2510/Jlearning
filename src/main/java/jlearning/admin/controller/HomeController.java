package jlearning.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jlearning.web.controller.BaseController;
import jlearning.model.User;
import jlearning.model.User.Role;
import jlearning.service.BlogService;
import jlearning.service.CourseService;
import jlearning.service.LessonService;
import jlearning.service.TestService;
import jlearning.service.UserService;

@PropertySource("classpath:messages.properties")
@Controller
@RequestMapping("/dashboard")
public class HomeController extends BaseController {
	@Value("${messages.notAlow}")
	private String msg_notAlow;
	@Autowired
	private UserService userService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private LessonService lessonService;
	@Autowired
	private BlogService blogService;
	@Autowired
	private TestService testService;
	@RequestMapping(value = { "", "/" })
	public String index(Model model, HttpServletRequest request, final RedirectAttributes redirectAttributes) {
		User user = loadCurrentUser(request);
		if (user == null) {
			return "redirect:/login";
		}
		if (user.getRole() != Role.ADMIN) {
			redirectAttributes.addFlashAttribute("msg", msg_notAlow);
			return "redirect:/";
		}
		int users= userService.loadUsers().size();
		int blogs= blogService.loadBlogs().size();
		int courses=courseService.loadCourses().size();
		int lessons = lessonService.loadAllLessons().size();
		int tests= testService.loadAllTest().size();
		model.addAttribute("userCount",users);
		model.addAttribute("blogCount",blogs);
		model.addAttribute("courseCount",courses);
		model.addAttribute("lessonCount",lessons);
		model.addAttribute("testCount",tests);
		
		
		return "views/admin/home/index";
	}

}
