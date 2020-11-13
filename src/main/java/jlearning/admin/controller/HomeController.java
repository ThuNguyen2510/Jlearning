package jlearning.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jlearning.web.controller.BaseController;
import jlearning.model.User;
import jlearning.model.User.Role;

@PropertySource("classpath:messages.properties")
@Controller
@RequestMapping("/dashboard")
public class HomeController extends BaseController {
	@Value("${messages.notAlow}")
	private String msg_notAlow;

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
		return "views/admin/home/index";
	}

}
