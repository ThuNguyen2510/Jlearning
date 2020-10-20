package jlearning.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(value = "exam")
public class ExamController {

	@RequestMapping("/exams")
	public String index() {
		return "views/web/exam/index";
	}

	@RequestMapping("/exams/1")
	public String exam() {
		return "views/web/exam/exam";
	}
	@RequestMapping("/exams/1/1")
	public String exam1() {
		return "views/web/exam/doExam";
	}

	
}