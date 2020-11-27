package jlearning.web.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jlearning.service.BlogService;
import jlearning.service.CourseService;
import jlearning.service.UserService;
import jlearning.validation.UserValidation;
import jlearning.bean.UserInfo;
import jlearning.model.Blog;
import jlearning.model.Course;
import jlearning.model.User;

@PropertySource("classpath:messages.properties")
@Controller(value = "home")
public class HomeController extends BaseController {
	private static final Logger logger = Logger.getLogger(HomeController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private BlogService blogService;

	private static final int ADMIN = 0;
	private static final int USER = 1;

	@Value("${messages.login}")
	private String msg_login;

	@Value("${messages.error_mail}")
	private String msg_error_mail;

	@Value("${messages.register}")
	private String msg_register;

	@Value("${Login_error}")
	private String Login_error;

	@RequestMapping("/")
	public String index(Model model, HttpServletRequest request) {

		checkObjectUser(model);

		List<Course> courses = courseService.loadCourses().subList(0, 3);
		model.addAttribute("courses", courses);

		List<Blog> blogs = blogService.loadBlogs();
		Collections.sort(blogs, new Comparator<Blog>() {

			public int compare(Blog o1, Blog o2) {
				// compare two instance of `Score` and return `int` as result.
				return o2.getComments().size() - o1.getComments().size();
			}
		});
		blogs = blogs.subList(0, 2);
		model.addAttribute("blogs", blogs);
		List<Blog> newBlogs = blogService.loadNewBlogs();
		newBlogs = newBlogs.subList(0, 3);

		model.addAttribute("news", newBlogs);

		return "views/web/home/index";
	}

	@RequestMapping("/login")
	public String login(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpServletRequest request, Model model, final RedirectAttributes redirectAttributes) {
		logger.info("login");
		User user = userService.findByEmailAndPassword(email, password.trim());
		if (user != null) {
			HttpSession session = request.getSession();
			redirectAttributes.addFlashAttribute("loginsuccess", msg_login);
			session.setAttribute("msg", user.getName().toUpperCase());
			session.setAttribute("currentUser", user.getId());
			session.setAttribute("userName",user.getName());
			session.setAttribute("level",user.getLevel());
			session.setAttribute("avatar",
					user.getAvatar() == null ? "https://riki.edu.vn/online/Content/images/icon/user.png"
							: user.getAvatar());

			if (user.getRole().toString().equals("ADMIN")) {
				session.setAttribute("roleUser", ADMIN);
				return "redirect:/dashboard";
			}

			session.setAttribute("roleUser", USER);

			return "redirect:/";
		} else {
			checkObjectUser(model);
			model.addAttribute("error", Login_error);
			return "views/web/home/index";
		}

	}

	@RequestMapping("/register")
	public String register(Model model) {

		return "views/web/home/index";
	}

	@RequestMapping("/registerProcess")
	public String register(@ModelAttribute("user") UserInfo userInfo, BindingResult result, Model model,
			final RedirectAttributes redirectAttributes) {
		logger.info("register");
		UserValidation userVali = new UserValidation();
		userVali.validate(userInfo, result);
		if (result.hasErrors()) {
			System.out.println(result.getFieldErrors());
		} else if (userService.createUser(userInfo.convertToUser()) == false) {
			model.addAttribute("error2", msg_error_mail);
		} else {
			userService.createUser(userInfo.convertToUser());
			model.addAttribute("registersuccess", msg_register);
		}

		return "views/web/home/index";
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, final RedirectAttributes redirectAttributes) {
		logger.info("logout");
		HttpSession session = request.getSession();
		redirectAttributes.addFlashAttribute("message", "Logout success!");
		session.removeAttribute("currentUser");
		session.removeAttribute("error");
		session.invalidate();
		return "redirect:/";
	}

}
