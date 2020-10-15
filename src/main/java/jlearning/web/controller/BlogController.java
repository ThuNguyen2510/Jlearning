package jlearning.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(value = "blog")
public class BlogController {

	@RequestMapping("/blogs/1")
	public String blog() {
		return "views/web/blog/post";
	}

	@RequestMapping("/blogs")
	public String blogs() {
		return "views/web/blog/index";
	}
}
