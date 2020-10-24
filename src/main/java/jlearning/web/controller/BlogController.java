package jlearning.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(value = "blog")
public class BlogController extends BaseController {

	@RequestMapping("/blogs/1")
	public String blog(Model model) {
		checkObjectUser(model);
		return "views/web/blog/post";
	}

	@RequestMapping("/blogs")
	public String blogs(Model model) {
		checkObjectUser(model);
		return "views/web/blog/index";
	}
}
