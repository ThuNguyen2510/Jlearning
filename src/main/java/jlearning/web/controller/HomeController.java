package jlearning.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(value = "home")
public class HomeController {
	@RequestMapping("/")
	public String index() {
		return "views/web/home/index";
	}

	
}
