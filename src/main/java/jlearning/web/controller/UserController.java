package jlearning.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jlearning.bean.UserInfo;
import jlearning.model.User;
import jlearning.model.User.Role;
import jlearning.service.UserService;
import jlearning.validation.UserValidation;

@Controller(value = "user")
public class UserController extends BaseController {
	@Autowired
	private UserService userService;
	private static final Logger logger = Logger.getLogger(UserController.class);

	@RequestMapping("/users/{id}")
	public String index(@PathVariable("id") int userId, Model model) {
		checkObjectUser(model);
		User u = userService.findById(userId);
		UserInfo userInfo = new UserInfo();
		userInfo.setId(u.getId());
		userInfo.setEmail(u.getEmail());
		userInfo.setName(u.getName());
		userInfo.setPassword(u.getPassword());
		userInfo.setLevel(u.getLevel());
		userInfo.setRole(u.getRole());
		model.addAttribute("userInfo", userInfo);
		return "views/web/user/index";
	}

	@RequestMapping("/users/{id}/edit")
	public String edit(@PathVariable("id") int userId,@ModelAttribute("userInfo") UserInfo user, BindingResult result, Model model,
			final RedirectAttributes redirectAttributes ) {
		checkObjectUser(model);
		UserValidation userVali = new UserValidation();
		userVali.validate(user, result);
		if(result.hasErrors()==false)
		{
			
			User us = user.convertToUser();
			us.setId(userId);
			us.setRole(Role.USER);
			if (userService.saveOrUpdate(us) == null) {
				logger.info("NOOO");
				redirectAttributes.addFlashAttribute("css", "Thất bại");
			} else {
				logger.info("YES");
				redirectAttributes.addFlashAttribute("css", "Thay đổi thông tin thành công");
			}
		}
		
		return "redirect:/users/"+userId;
	}

	@RequestMapping("/users/{id}/examHis")
	public String exams(Model model, @PathVariable("id") int userId) {
		checkObjectUser(model);
		return "views/web/user/examHistory";
	}

	@RequestMapping("/users/{id}/lessonHis")
	public String lessons(@PathVariable("id") int userId, Model model) {
		checkObjectUser(model);
		return "views/web/user/lessonHistory";
	}

	@RequestMapping("/users/{id}/blogs")
	public String blogs(@PathVariable("id") int userId, Model model) {
		checkObjectUser(model);
		return "views/web/user/myBlogs";
	}
}
