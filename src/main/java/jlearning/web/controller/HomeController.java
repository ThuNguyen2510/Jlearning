package jlearning.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jlearning.service.UserService;
import jlearning.model.User;

@PropertySource("classpath:messages.properties")
@Controller(value = "home")
public class HomeController {
	private static final Logger logger = Logger.getLogger(HomeController.class);
	
	@Autowired
	private UserService userService;
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
	public String index(Model model) {
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
			
			/*
			 * if (user.getRole().toString().equals("ADMIN")) {
			 * session.setAttribute("roleUser", ADMIN); return "redirect:/dashboard"; }
			 */
			session.setAttribute("roleUser", USER);
			session.removeAttribute("error");
			return "redirect:/";
		}else {
			HttpSession session = request.getSession();
			session.setAttribute("error", Login_error);
			return "views/web/home/index";
		}
		
	}
	
	@RequestMapping("/register")
	public String register()
	{
		return "views/web/home/index";
	}
	
	
}
