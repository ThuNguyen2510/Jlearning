package jlearning.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jlearning.bean.AnswerInfo;
import jlearning.bean.AnswerList;
import jlearning.model.Test;
import jlearning.model.Test.Type;
import jlearning.service.QuestionService;
import jlearning.service.TestService;

@Controller(value = "exam")
public class ExamController extends BaseController {

	private static final Logger logger = Logger.getLogger(ExamController.class);
	@Autowired
	private TestService testService;
	
	@Autowired
	private QuestionService questionService;
	
	@RequestMapping("/exams")
	public String index(Model model) {
		checkObjectUser(model);
		return "views/web/exam/index";
	}

	@RequestMapping("/exams/1") /* cap do 1 */
	public String exam(Model model) {
		checkObjectUser(model);
		return "views/web/exam/exam";
	}

	@RequestMapping("/exams/1/1") /* de 1 trong cap do 1 */
	public String exam1(Model model) {
		checkObjectUser(model);
		return "views/web/exam/viewExam";
	}
	
	@RequestMapping("/exams/doExam") /* Thi */
	public String test(Model model) {
		checkObjectUser(model);
		return "views/web/exam/doExam";
	}
	
	@RequestMapping("/testLevel") /* Test Level */
	public String testLevel(Model model) {
		checkObjectUser(model);
		Test testLevel = testService.findByType(Type.LEVEL);
		model.addAttribute("testLevel",testLevel);
		AnswerList ansForm = new AnswerList();
		for(int i=0;i<5;i++)
		{
			ansForm.addAnswer(new AnswerInfo(""));
		}
		model.addAttribute("form",ansForm);
		return "views/web/exam/testLevel";
	}
	
	@RequestMapping("/testLevel/result")
	public String result(Model model,HttpServletRequest request ) {
		checkObjectUser(model);
		String []questionIds = request.getParameterValues("questionId");
		for(String id : questionIds) {
			logger.info(id);
		}
		return "views/web/exam/testLevel";
	}

	
}