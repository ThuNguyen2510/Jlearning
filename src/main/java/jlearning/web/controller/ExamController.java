package jlearning.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jlearning.bean.AnswerInfo;
import jlearning.bean.AnswerList;
import jlearning.model.Course;
import jlearning.model.Lesson;
import jlearning.model.Question;
import jlearning.model.Result;
import jlearning.model.Test;
import jlearning.model.Test.Type;
import jlearning.model.User;
import jlearning.service.CourseService;
import jlearning.service.QuestionService;
import jlearning.service.ResultService;
import jlearning.service.TestService;
import jlearning.service.UserService;

@Controller(value = "exam")
public class ExamController extends BaseController {

	private static final Logger logger = Logger.getLogger(ExamController.class);
	@Autowired
	private TestService testService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private ResultService resultService;

	@Autowired
	private UserService userService;

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

	@RequestMapping("/tests/{id}/answer")
	public String lessonAnswer(@PathVariable("id") int testId, Model model, HttpServletRequest request) {
		checkObjectUser(model);
		HttpSession session = request.getSession();
		Result rs = null;
		if (session.getAttribute("currentUser") != null) {
			if (session.getAttribute("notifi") != null) {
				model.addAttribute("notifi", session.getAttribute("notifi"));
				model.addAttribute("score", session.getAttribute("score"));
				//remove
				
			}
			/*
			 * if(session.getAttribute("userAns") != null) { model.addAttribute("userAns",
			 * session.getAttribute("userAns"));
			 * logger.info(session.getAttribute("userAns"));
			 * 
			 * //remove
			 * 
			 * }
			 */
			model.addAttribute("hashMap",session.getAttribute("hashMap"));
			logger.info(session.getAttribute("hashMap"));
			Test test = testService.findAndLoad(testId);
			Course course = test.getLesson().getCourse();
			Lesson lesson=test.getLesson();
			model.addAttribute("course",course);
			model.addAttribute("lesson",lesson);
			
			model.addAttribute("test", test);
			
			if(testId<3) {
				
				List<Question>  ques= (List<Question>) session.getAttribute("testAlphabet");
				model.addAttribute("ques",ques);
				session.removeAttribute("testAlphabet");
				
			}
		} else {
			model.addAttribute("chuaLogin", "Bạn chưa đăng nhập");
		}
		
		

		return "views/web/lesson/answer";
	}

	@RequestMapping("/exams/{testId}/result") /* Test Lesson */
	public String test(Model model, @PathVariable("testId") int testId, HttpServletRequest request) {
		checkObjectUser(model);
		HttpSession session = request.getSession();
		int score = 0;
		int count = 0;
		String notifi="";
		String[] questionTextIds = request.getParameterValues("questionTextId");
		List<Object> userAns = new ArrayList<Object>();
		if (questionTextIds != null) {
			for (String id : questionTextIds) {

				String answerContentCorrect = questionService.findContentOfAnswerCorrect(Integer.parseInt(id));
				if (request.getParameter("answers[" + id + "].content") != null) {
					userAns.add(request.getParameter("answers[" + id + "].content"));
					if (answerContentCorrect
							.compareToIgnoreCase(request.getParameter("answers[" + id + "].content")) == 0) {
						score++;
						count++;
					}
				}
			}

		}

		HashMap<String, String> hashMap = new HashMap<String, String>();
		String[] questionIds = request.getParameterValues("questionId");
		if (questionIds != null) {
			for (String id : questionIds) {

				int answerIdCorrect = questionService.findAnswerIdCorrect(Integer.parseInt(id));
				if(request.getParameter("answer_" + id) == null)hashMap.put(id,"-1");
				if (request.getParameter("answer_" + id) != null) {
					hashMap.put(id,request.getParameter("answer_" + id));
					userAns.add(request.getParameter("answer_" + id));
					if (answerIdCorrect == Integer.parseInt(request.getParameter("answer_" + id))) {
						score++;
						count++;
					}
				}
				
				
			}
		}

		session.setAttribute("score", score);
		session.setAttribute("hashMap", hashMap);
		Test test = testService.findById(testId);
		User user = userService.findById((int) session.getAttribute("currentUser"));

		if (checkResultHasTestInLesson(user, test.getLesson().getId()) == null) {
			Result rs = new Result();
			rs.setScore(score);
			rs.setUser(user);
			rs.setTest(test);
			resultService.saveOrUpdate(rs);
			session.setAttribute("notifi", notifi);

		} else {
			Result oldRs = checkResultHasTestInLesson(user, test.getLesson().getId());
			if (score > oldRs.getScore()) {
				oldRs.setScore(score);
				resultService.saveOrUpdate(oldRs);
				if (score >= 6 && oldRs.getScore() < 6) {
					notifi = "Bạn được học bài tiếp theo!";
					session.setAttribute("notifi", notifi);

				} else {
					notifi = "Điểm cao hơn lần trước rồi nè bạn! :))) ";
					session.setAttribute("notifi", notifi);
				}

			} else {
				notifi = "Điểm thấp hơn lần trước rồi bạn! :((( ";
				session.setAttribute("notifi", notifi);
			}

		}
		if (testService.checkTestFinalOfLesson(testId)) {
			logger.info("check");
		}
		session.setAttribute("userAns", userAns);
		/*
		 * int lessonLength = test.getLesson().getCourse().getLessons().size(); int id=
		 * test.getLesson().getTests().get(lessonLength - 1).getId(); if (test ==
		 * testService.findById(id)) { if (score >= 6) { user.setLevel(user.getLevel() +
		 * 1); model.addAttribute("upCourse", "Bạn được học khóa học ở cấp độ mới!");
		 * userService.saveOrUpdate(user); } }
		 */

		int id2 = test.getLesson().getId();
		int id1 = Integer.parseInt(session.getAttribute("courseId").toString());
		return "redirect:/tests/" + id2 + "/answer";
	}

	@RequestMapping("/testLevel") /* Test Level */
	public String testLevel(Model model, HttpServletRequest request) {
		checkObjectUser(model);
		Test testLevel = testService.findByType(Type.LEVEL);
		model.addAttribute("testLevel", testLevel);
		HttpSession session = request.getSession();
		session.setAttribute("testLevel", testLevel);
		AnswerList ansForm = new AnswerList();
		List<AnswerInfo> list = new ArrayList<AnswerInfo>();
		for (int i = 0; i < testLevel.getQuestions().size(); i++) {
			if (testLevel.getQuestions().get(i).getAnswers().size() == 1)
				list.add(new AnswerInfo());
		}

		ansForm.setAnswers(list);
		model.addAttribute("form", ansForm);
		return "views/web/exam/testLevel";
	}

	@RequestMapping("/testLevel/result")
	public String result(Model model, HttpServletRequest request) {
		checkObjectUser(model);
		HttpSession session = request.getSession();
		int score = 0;
		int count = 0;
		int[] level = { 0, 0, 0, 0, 0, 0 };
		String[] questionIds = request.getParameterValues("questionId");
		for (String id : questionIds) {
			int answerIdCorrect = questionService.findAnswerIdCorrect(Integer.parseInt(id));
			if (request.getParameter("answer_" + id) != null) {
				if (answerIdCorrect == Integer.parseInt(request.getParameter("answer_" + id))) {

					Question q = questionService.findById(Integer.parseInt(id));
					if (q.getLevel() == 0)
						level[0] += 1;
					if (q.getLevel() == 1)
						level[1] += 1;
					if (q.getLevel() == 2)
						level[2] += 1;
					if (q.getLevel() == 3)
						level[3] += 1;
					if (q.getLevel() == 4)
						level[4] += 1;
					if (q.getLevel() == 5)
						level[5] += 1;

					score++;
					count++;
				}
			}

		}
		String[] questionTextIds = request.getParameterValues("questionTextId");
		for (String id : questionTextIds) {

			String answerContentCorrect = questionService.findContentOfAnswerCorrect(Integer.parseInt(id));
			if (request.getParameter("answers[" + id + "].content") != null) {
				if (answerContentCorrect
						.compareToIgnoreCase(request.getParameter("answers[" + id + "].content")) == 0) {
					Question q = questionService.findById(Integer.parseInt(id));
					if (q.getLevel() == 0)
						level[0] += 1;
					if (q.getLevel() == 1)
						level[1] += 1;
					if (q.getLevel() == 2)
						level[2] += 1;
					if (q.getLevel() == 3)
						level[3] += 1;
					if (q.getLevel() == 4)
						level[4] += 1;
					if (q.getLevel() == 5)
						level[5] += 1;

					score++;
					count++;
				}
			}

		}

		logger.info("Diem " + score);
		logger.info("So cau " + count);
		logger.info("Level " + getIndexOfLargest(level));

		Result rs = new Result();
		User user = userService.findById((int) session.getAttribute("currentUser"));
		rs.setScore(score);
		rs.setUser(user);
		rs.setTest((Test) session.getAttribute("testLevel"));
		user.setLevel(getIndexOfLargest(level) + 1);
		if (resultService.create(rs) != null) {
			model.addAttribute("result", rs);
			String[] course = { "Cấp độ cơ bản", "Cấp độ N5", "Cấp độ N4", "Cấp độ N3", "Cấp độ N2", "Cấp độ N1" };
			model.addAttribute("level", course[getIndexOfLargest(level)]);
		}
		userService.saveOrUpdate(user);
		session.removeAttribute("testLevel");
		return "views/web/exam/testLevel";
	}

	public int getIndexOfLargest(int[] array) {
		if (array == null || array.length == 0)
			return -1; // null or empty

		int largest = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] > array[largest])
				largest = i;
		}
		return largest; // position of the first largest found
	}

}