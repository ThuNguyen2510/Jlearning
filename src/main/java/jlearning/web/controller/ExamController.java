package jlearning.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(value = "exam")
public class ExamController {

	@RequestMapping("/exams")
	public String index() {
		return "views/web/exam/index";
	}

	@RequestMapping("/exams/1") /* cap do 1 */
	public String exam() {
		return "views/web/exam/exam";
	}

	@RequestMapping("/exams/1/1") /* de 1 trong cap do 1 */
	public String exam1() {
		return "views/web/exam/viewExam";
	}
	
	@RequestMapping("/exams/doExam") /* Thi */
	public String test() {
		return "views/web/exam/doExam";
	}
	
	@RequestMapping("/testLevel") /* Test Level */
	public String testLevel() {
		return "views/web/exam/testLevel";
	}
	

	
}