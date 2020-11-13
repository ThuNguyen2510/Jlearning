package jlearning.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jlearning.bean.AnswerInfo;
import jlearning.bean.AnswerList;
import jlearning.bean.ExamInfo;
import jlearning.model.Course;
import jlearning.model.Lesson;
import jlearning.model.Question;
import jlearning.model.Question.Part;
import jlearning.model.Result;
import jlearning.model.Test;
import jlearning.model.Test.Type;
import jlearning.model.User;
import jlearning.service.CourseService;
import jlearning.service.LessonService;
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

	@Autowired
	private LessonService lessonService;

	@Autowired
	private CourseService courseService;

	@RequestMapping("/exams")
	public String index(Model model) {
		checkObjectUser(model);
		return "views/web/exam/index";
	}

	@RequestMapping("/exams/{level}") /* cap do 1 */
	public String exam(@PathVariable("level") int level, Model model) {
		checkObjectUser(model);
		List<Test> exams = testService.findByLevel(level);

		List<ExamInfo> examInfos = new ArrayList<ExamInfo>();
		for (int i = 0; i < exams.size(); i++) {
			ExamInfo e = new ExamInfo();
			Test ex = testService.findAndLoad(exams.get(i).getId());
			e.setId(ex.getId());
			e.setName(ex.getName());
			e.setScore(ex.getQuestionQuantity());
			e.setTime(ex.getTime());
			e.setPartCount(partCount(ex));
			examInfos.add(e);
		}
		model.addAttribute("level", 6 - level);
		model.addAttribute("exams", examInfos);
		return "views/web/exam/exam";
	}

	@RequestMapping("/exams/{level}/{examId}") /* de 1 trong cap do 1 */
	public String exam1(@PathVariable("level") int level, @PathVariable("examId") int testId, Model model) {
		checkObjectUser(model);
		Test test = testService.findAndLoad(testId);
		model.addAttribute("test", test);
		int vocabQues = 0;
		int gramQues = 0;
		int readQues = 0;
		int lisQues = 0;
		for (int i = 0; i < test.getQuestions().size(); i++) {
			Question q = test.getQuestions().get(i);
			if (q.getPart() == Part.vocab) {
				vocabQues++;
			} else if (q.getPart() == Part.gram) {
				gramQues++;
			} else if (q.getPart() == Part.read) {
				readQues++;
			} else {
				lisQues++;
			}
		}
		model.addAttribute("vocabQues", vocabQues);
		model.addAttribute("gramQues", gramQues);
		model.addAttribute("readQues", readQues);
		model.addAttribute("lisQues", lisQues);
		List<Result> ranking = resultService.rankingByTest(testId);
		model.addAttribute("rank", ranking);
		return "views/web/exam/viewExam";
	}

	@RequestMapping("/exams/{examId}/doExam/{part}") /* Thi */
	public String testExam(@PathVariable("part") String part, Model model, @PathVariable("examId") int testId,
			HttpServletRequest request) throws ParseException {
		checkObjectUser(model);
		HttpSession session = request.getSession();
		Test test = testService.findAndLoad(testId);
		model.addAttribute("test", test);
		List<Question> vocabQues = new ArrayList<Question>();
		List<Question> gramQues = new ArrayList<Question>();
		List<Question> readQues = new ArrayList<Question>();
		List<Question> lisQues = new ArrayList<Question>();
		for (int i = 0; i < test.getQuestions().size(); i++) {
			Question q = test.getQuestions().get(i);
			if (q.getPart() == Part.vocab) {
				vocabQues.add(q);
			} else if (q.getPart() == Part.gram) {
				gramQues.add(q);
			} else if (q.getPart() == Part.read) {
				readQues.add(q);
			} else {
				lisQues.add(q);
			}
		}
		if (part.compareTo("vocab") == 0) {
			logger.info("TUVUNG");
			model.addAttribute("vocabPage", "vocab");

		}
		if (part.compareTo("gram") == 0) {
			logger.info("GRAM");
			model.addAttribute("gramPage", "gram");

		}
		if (part.compareTo("read") == 0) {
			logger.info("READ");
			model.addAttribute("readPage", "read");

		}
		if (part.compareTo("listening") == 0) {
			logger.info("LISTEN");
			model.addAttribute("lisPage", "listening");

		}

		checkTime(session, model);
		model.addAttribute("vocabQues", vocabQues);
		model.addAttribute("gramQues", gramQues);
		model.addAttribute("readQues", readQues);
		model.addAttribute("lisQues", lisQues);

		return "views/web/exam/doExam";
	}

	private void checkTime(HttpSession session, Model model) {
		if (session.getAttribute("start_") == null) {
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("MMM dd YYYY hh:mm:ss aa");
			logger.info("TIME " + formatter.format(date));
			session.setAttribute("start_", formatter.format(date));
			session.setAttribute("start", System.nanoTime());
		} else {
			Date d = new Date(session.getAttribute("start_").toString());
			if (new Date().getTime() >= d.getTime() + 30 * 60000) {
				model.addAttribute("timeUp", "end");
				session.removeAttribute("start_");
			}
		}
	}

	@RequestMapping("/exams/{examId}/doExam/{part}/save") /* Thi */
	public String saveExam(Model model, @PathVariable("examId") int testId, @PathVariable("part") String part,
			HttpServletRequest request) {
		checkObjectUser(model);
		HttpSession session = request.getSession();
		Test test = testService.findAndLoad(testId);
		model.addAttribute("test", test);
		List<Question> vocabQues = new ArrayList<Question>();
		List<Question> gramQues = new ArrayList<Question>();
		List<Question> readQues = new ArrayList<Question>();
		List<Question> lisQues = new ArrayList<Question>();
		for (int i = 0; i < test.getQuestions().size(); i++) {
			Question q = test.getQuestions().get(i);
			if (q.getPart() == Part.vocab) {
				vocabQues.add(q);
			} else if (q.getPart() == Part.gram) {
				gramQues.add(q);
			} else if (q.getPart() == Part.read) {
				readQues.add(q);
			} else {
				lisQues.add(q);
			}
		}
		String next = "done";
		if (part.compareTo("vocab") == 0) {
			if (gramQues.size() > 0)
				next = "gram";

		}
		if (part.compareTo("gram") == 0) {
			if (readQues.size() > 0)
				next = "read";
		}
		if (part.compareTo("read") == 0) {
			if (lisQues.size() > 0)
				next = "listening";
		}
		if (part.compareTo("listening") == 0) {
			next = "done";
		}
		model.addAttribute("next", next);
		checkTime(session, model);
		model.addAttribute("vocabQues", vocabQues);
		model.addAttribute("gramQues", gramQues);
		model.addAttribute("readQues", readQues);
		model.addAttribute("lisQues", lisQues);
		if (session.getAttribute("quesAnsExam") != null) {
			HashMap<String, String> hashMap = (HashMap<String, String>) session.getAttribute("quesAnsExam");
			String[] questionIds = request.getParameterValues("questionId");

			if (questionIds != null) {
				for (String id : questionIds) {
					logger.info("ID " + id);
					if (request.getParameter("answer_" + id) == null)
						hashMap.put(id, "-1");
					if (request.getParameter("answer_" + id) != null) {
						hashMap.put(id, request.getParameter("answer_" + id));
					}

				}
			}

			session.setAttribute("quesAnsExam", hashMap);
		} else {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			String[] questionIds = request.getParameterValues("questionId");
			logger.info(questionIds);
			if (questionIds != null) {
				for (String id : questionIds) {

					if (request.getParameter("answer_" + id) == null)
						hashMap.put(id, "-1");
					if (request.getParameter("answer_" + id) != null) {
						hashMap.put(id, request.getParameter("answer_" + id));
					}

				}
			}
			session.setAttribute("quesAnsExam", hashMap);

		}

		return "views/web/exam/savePart";

	}

	@RequestMapping("/exams/{examId}/examResult") /* Thi */
	public String resultExam(Model model, @PathVariable("examId") int testId, HttpServletRequest request,
			final RedirectAttributes redirectAttributes) {
		HttpSession session = request.getSession();
		HashMap<String, String> hash = (HashMap<String, String>) session.getAttribute("quesAnsExam");
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		int count4 = 0;
		int score = 0;
		List<Integer> count = new ArrayList<Integer>();
		for (Map.Entry<String, String> entry : hash.entrySet()) {
			Question q = questionService.findById(Integer.parseInt(entry.getKey()));
			int answerIdCorrect = questionService.findAnswerIdCorrect(Integer.parseInt(entry.getKey()));
			if (answerIdCorrect == Integer.parseInt(entry.getValue())) {
				score++;
				if (q.getPart() == Part.vocab) {
					count1++;
				} else if (q.getPart() == Part.gram) {
					count2++;
				} else if (q.getPart() == Part.read) {
					count3++;
				} else {
					count4++;
				}
			}
		}
		count.add(count1);
		count.add(count2);
		count.add(count3);
		count.add(count4);
		Result rs = new Result();
		Test t = testService.findById(testId);
		rs.setScore(score);
		rs.setTest(t);
		User u = userService.findById(Integer.parseInt(session.getAttribute("currentUser").toString()));
		rs.setUser(u);
		session.removeAttribute("start_");
		session.removeAttribute("quesAnsExam");
		resultService.create(rs);
		int level = 6 - t.getLevel();
		redirectAttributes.addFlashAttribute("resultExam", "re");
		redirectAttributes.addFlashAttribute("result", new Date());
		redirectAttributes.addFlashAttribute("count", count);
		redirectAttributes.addFlashAttribute("score",score);
		logger.info("COUNT"+count.get(0));
		redirectAttributes.addFlashAttribute("nameTest", t.getName());
		redirectAttributes.addFlashAttribute("userName", u.getName());
		return "redirect:/exams/" + level + "/" + testId;
	}

	@RequestMapping("/tests/{id}/answer")
	public String lessonAnswer(@PathVariable("id") int testId, Model model, HttpServletRequest request) {
		checkObjectUser(model);
		HttpSession session = request.getSession();
		Result rs = null;
		String notifi = null;
		int score = 0;
		if (session.getAttribute("currentUser") != null) {
			if (session.getAttribute("notifi") != null) {

				notifi = (String) session.getAttribute("notifi");

				score = (Integer) session.getAttribute("score");
				model.addAttribute("notifi", notifi);
				model.addAttribute("score", score);

				/*
				 * session.removeAttribute("notifi"); session.removeAttribute("score");
				 */
				// remove

			}

			if (session.getAttribute("userAns") != null) {
				model.addAttribute("userAns", session.getAttribute("userAns"));

				session.removeAttribute("userAns");
				// remove

			}
			HashMap<String, String> hashMap = new HashMap<String, String>();
			if (session.getAttribute("hashMap") != null) {
				hashMap = (HashMap<String, String>) session.getAttribute("hashMap");
			}
			session.removeAttribute("hashMap");
			TreeMap<String, String> tree = new TreeMap<String, String>();
			tree.putAll(hashMap);
			List<String> list = new ArrayList<String>();
			for (Map.Entry<String, String> entry : tree.entrySet()) {
				list.add(entry.getValue());
			}
			model.addAttribute("list", list);
			Test test = testService.findAndLoad(testId);
			Course course = test.getLesson().getCourse();
			Lesson lesson = test.getLesson();
			int courseId = course.getId();
			if (notifi != null) {
				int pos = course.getLessons().indexOf(lesson);
				int nextLessonId = 0;
				if (testService.checkTestFinalOfLesson(testId)) {
					if (score >= 6) {
						User user = userService.findById((int) session.getAttribute("currentUser"));

						course = courseService.findById(courseId + 1);
						user.setLevel(course.getLevel());
						nextLessonId = course.getLessons().get(0).getId();
						notifi = "Bạn được học khóa học ở cấp độ mới: " + course.getName();
						model.addAttribute("notifi", notifi);
						model.addAttribute("nextLessonId", nextLessonId);
						userService.saveOrUpdate(user);

					}
				} else {
					nextLessonId = course.getLessons().get(pos + 1).getId();
					if (lessonService.findById(nextLessonId) != null) {
						model.addAttribute("nextLessonId", nextLessonId);
					}

				}
			}
			model.addAttribute("course", course);
			model.addAttribute("lesson", lesson);
			model.addAttribute("test", test);

			if (testId < 3) {
				List<Question> ques = (List<Question>) session.getAttribute("testAlphabet");
				model.addAttribute("ques", ques);
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
		String notifi = "";
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
				if (request.getParameter("answer_" + id) == null)
					hashMap.put(id, "-1");
				if (request.getParameter("answer_" + id) != null) {
					hashMap.put(id, request.getParameter("answer_" + id));
					userAns.add(request.getParameter("answer_" + id));
					if (answerIdCorrect == Integer.parseInt(request.getParameter("answer_" + id))) {
						score++;
						count++;
					}
				}

			}
		}

		session.setAttribute("score", score);
		session.setAttribute("userAns", userAns);// for questionTest
		session.setAttribute("hashMap", hashMap);// for question
		Test test = testService.findById(testId);
		User user = userService.findById((int) session.getAttribute("currentUser"));

		if (checkResultHasTestInLesson(user, test.getLesson().getId()) == null) {
			Result rs = new Result();
			rs.setScore(score);
			rs.setUser(user);
			rs.setTest(test);
			resultService.saveOrUpdate(rs);
			if (score >= 6) {
				logger.info("SCORE: " + score);
				notifi = "Bạn được học bài tiếp theo!";
			} else {
				notifi = "Bạn chưa đủ điểm để học bài tiếp theo!";
			}
			session.setAttribute("notifi", notifi);

		} else {
			Result oldRs = checkResultHasTestInLesson(user, test.getLesson().getId());
			
			if (oldRs != null)
				
			if (score >= oldRs.getScore()) {
				oldRs.setScore(score);
				resultService.saveOrUpdate(oldRs);
				if (score >= 6 && oldRs.getScore() < 6) {

					notifi = "Bạn được học bài tiếp theo!";
					session.setAttribute("notifi", notifi);

				} else if (score >= 6) {
					notifi = "Điểm cao hơn lần trước rồi nè bạn! :))) ";
					session.setAttribute("notifi", notifi);

				}

			} else {
				notifi = "Điểm thấp hơn lần trước rồi bạn! :((( ";
				session.setAttribute("notifi", notifi);
			}

		}
		int id2 = test.getLesson().getId();
		int id1 = Integer.parseInt(session.getAttribute("courseId").toString());
		return "redirect:/tests/" + id2 + "/answer";
	}

	@RequestMapping("/testLevel") /* Test Level */
	public String testLevel(Model model, HttpServletRequest request) {
		checkObjectUser(model);
		// check level to do test
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
		TreeMap<String, String> tree = new TreeMap<String, String>();
		String[] questionIds = request.getParameterValues("questionId");
		for (String id : questionIds) {
			int answerIdCorrect = questionService.findAnswerIdCorrect(Integer.parseInt(id));
			if (request.getParameter("answer_" + id) != null) {
				tree.put(id, request.getParameter("answer_" + id));
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
			} else {
				tree.put(id, "-1");
			}

		}
		String[] questionTextIds = request.getParameterValues("questionTextId");

		for (String id : questionTextIds) {

			String answerContentCorrect = questionService.findContentOfAnswerCorrect(Integer.parseInt(id));

			if (request.getParameter("answers[" + id + "].content") != null) {

				tree.put(id, request.getParameter("answers[" + id + "].content"));
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
			} else {
				tree.put(id, "-1");
			}

		}
		List<String> list = new ArrayList<String>();
		for (Map.Entry<String, String> entry : tree.entrySet()) {

			if (entry.getValue() != null)
				list.add(entry.getValue());

		}
		model.addAttribute("list", list);

		Result rs = new Result();
		User user = userService.findById((int) session.getAttribute("currentUser"));
		Test test = testService.findAndLoad(((Test) session.getAttribute("testLevel")).getId());
		rs.setScore(score);
		rs.setUser(user);
		rs.setTest((Test) session.getAttribute("testLevel"));
		int l = 1;
		int index = 0;
		if (user.getLevel() == 0) {
			if (level[0] >= 3) {
				if (level[1] >= 3) {
					l = 2;
					index = 1;
					if (level[2] >= 3) {
						l = 3;
						index = 2;
						if (level[3] >= 3) {
							l = 4;
							index = 3;
							if (level[4] >= 3) {
								l = 5;
								index = 4;
								if (level[5] >= 3) {
									l = 6;
									index = 5;
								}
							}
						}
					}
				}
			}
			user.setLevel(l);
			userService.saveOrUpdate(user);
		}

		if (resultService.create(rs) != null) {
			model.addAttribute("result", rs);
			String[] course = { "Cấp độ cơ bản", "Cấp độ N5", "Cấp độ N4", "Cấp độ N3", "Cấp độ N2", "Cấp độ N1" };
			model.addAttribute("level", course[index]);
		}
		model.addAttribute("testLevel", test);
		Course course = courseService.findByLevel(getIndexOfLargest(level) + 1);
		model.addAttribute("courseId", course.getId());
		session.removeAttribute("testLevel");
		return "views/web/exam/levelResult";
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

	private int partCount(Test test) {
		int vocabQues = 0;
		int gramQues = 0;
		int readQues = 0;
		int lisQues = 0;
		for (int i = 0; i < test.getQuestions().size(); i++) {
			Question q = test.getQuestions().get(i);
			if (q.getPart() == Part.vocab) {
				vocabQues++;
			} else if (q.getPart() == Part.gram) {
				gramQues++;
			} else if (q.getPart() == Part.read) {
				readQues++;
			} else {
				lisQues++;
			}
		}
		int count = 0;
		if (vocabQues > 0)
			count++;
		if (gramQues > 0)
			count++;
		if (readQues > 0)
			count++;
		if (lisQues > 0)
			count++;
		return count;

	}

}