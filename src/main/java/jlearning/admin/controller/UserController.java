package jlearning.admin.controller;

import java.util.ArrayList;
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

import jlearning.model.Result;
import jlearning.model.User;
import jlearning.model.Test.Type;
import jlearning.model.User.Role;
import jlearning.service.UserService;
import jlearning.validation.UserValidation;

@Controller
@RequestMapping("/admin/users")
public class UserController {
	private static final Logger logger = Logger.getLogger(UserController.class);
	@Autowired
	private UserService userService;

	@GetMapping(value = { "", "/" })
	public String index(Model model) {
		List<User> users = userService.loadUsers();
		model.addAttribute("users", users);
		return "views/admin/user/users";
	}

	@GetMapping("/{id}")
	public String show(@PathVariable("id") int id, Model model) {
		User user = userService.findById(id);
		if (user == null)
			return "views/admin/error/404";
		model.addAttribute("user", user);
		List<Result> rsLevels = new ArrayList<Result>();
		List<Result> rsLessons = new ArrayList<Result>();
		List<Result> rsExams = new ArrayList<Result>();
		for (int i = 0; i < user.getResults().size(); i++) {
			Result rs = user.getResults().get(i);
			if (rs.getTest().getType() == Type.LEVEL) {
				rsLevels.add(rs);
			}
			if (rs.getTest().getType() == Type.LESSON) {
				rsLessons.add(rs);
			}
			if (rs.getTest().getType() == Type.EXAM) {
				rsExams.add(rs);
			}
		}
		model.addAttribute("rsLevels", rsLevels);
		model.addAttribute("rsLessons", rsLessons);
		model.addAttribute("rsExams", rsExams);
		return "views/admin/user/user";

	}

	@GetMapping(value = "/{id}/delete")
	public String deleteUser(@PathVariable("id") Integer id, final RedirectAttributes redirectAttributes) {
		User user = userService.findById(id);
		String typeCss = "error";
		String message = "User not found!";
		if (user == null) {
			redirectAttributes.addFlashAttribute("css", typeCss);
			redirectAttributes.addFlashAttribute("msg", message);
			return "redirect:/admin/users";
		}
		typeCss = "error";
		message = "Fail to delete user!";
		if (userService.deleteUser(id)) {
			typeCss = "success";
			message = "User is deleted!";
		}
		redirectAttributes.addFlashAttribute("css", typeCss);
		redirectAttributes.addFlashAttribute("msg", message);
		return "redirect:/admin/users";

	}

	@GetMapping(value = "/add")
	public String newStudent(Model model) {
		User user = new User();
		model.addAttribute("userForm", user);
		model.addAttribute("status", "add");
		return "views/admin/user/user-form";
	}

	@RequestMapping(value = "/saveupdate")
	public String saveOrUpdate(@ModelAttribute("userForm") User user, BindingResult result, Model model,
			final RedirectAttributes redirectAttributes) {
		String typeCss = "error";
		String message = "Fail to create user! Maybe new email is existed!";
		UserValidation userVali = new UserValidation();
		userVali.validate_(user, result);
		if (userService.saveOrUpdate(user) == null) {
			redirectAttributes.addFlashAttribute("css", typeCss);
			redirectAttributes.addFlashAttribute("msg", message);
			return "redirect:/admin/users";
		}
		if (result.hasErrors()) {
			return "views/admin/user/user-form";
		} else if (userService.checkEmailExist(user.getEmail().trim())) {
			logger.info("EMAIL");
			model.addAttribute("error", "Đã tồn tại email này!");
			return "views/admin/user/user-form";
		} else {

			typeCss = "success";
			message = "User is created successfully!!";
			redirectAttributes.addFlashAttribute("css", typeCss);
			redirectAttributes.addFlashAttribute("msg", message);
			return "redirect:/admin/users";
		}

	}

	@GetMapping(value = "/{id}/edit")
	public String editUser(@PathVariable("id") int id, Model model, final RedirectAttributes redirectAttributes) {
		String typeCss = "error";
		String message = "User not found!";
		User user = userService.findById(id);
		if (user != null) {
			model.addAttribute("userForm", user);
			model.addAttribute("status", "update");
			return "views/admin/user/user-form";
		}
		redirectAttributes.addFlashAttribute("css", typeCss);
		redirectAttributes.addFlashAttribute("msg", message);
		return "redirect:/admin/users";

	}

}