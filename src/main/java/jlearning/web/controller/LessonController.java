package jlearning.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jlearning.bean.AnswerInfo;
import jlearning.bean.AnswerList;
import jlearning.model.Alphabet;
import jlearning.model.Blog;
import jlearning.model.Lesson;
import jlearning.model.Question;
import jlearning.model.Result;
import jlearning.model.Test;
import jlearning.model.User;
import jlearning.model.Character;
import jlearning.model.Course;
import jlearning.model.History;
import jlearning.service.AlphabetService;
import jlearning.service.CourseService;
import jlearning.service.HistoryService;
import jlearning.service.LessonService;
import jlearning.service.UserService;

@Controller(value = "lesson")
public class LessonController extends BaseController {
	private static final Logger logger = Logger.getLogger(LessonController.class);
	@Autowired
	private LessonService lessonService;

	@Autowired
	private AlphabetService alphabetService;

	@Autowired
	private UserService userService;

	@Autowired
	private HistoryService historyService;

	@RequestMapping("/courses/{id1}/lessons/{id2}")
	public String index(@PathVariable("id1") int courseId, @PathVariable("id2") int lessonId, Model model,
			HttpServletRequest request) {
		checkObjectUser(model);
		HttpSession session = request.getSession();
		{

			if (session.getAttribute("currentUser") != null) {
				int userId = (int) session.getAttribute("currentUser");
				User user = userService.findById(userId);
				if (user != null) {
					{
						// hoc bai hoc dang hoc
						Lesson lesson = lessonService.findById(lessonId);
						Course course = lesson.getCourse();
						session.setAttribute("courseId", course.getId());
						{

							if (lesson.equals(course.getLessons().get(0))) {
								// la lesson1 thi cho hoc

								if (courseId == 1 && lessonId < 3) {

									Alphabet alphabet = alphabetService.findById(lessonId);
									model.addAttribute("alphabet", alphabet);
									List<Character> list1 = alphabet.getCharacters().subList(0,
											alphabet.getCharacters().size() / 2);
									List<Character> list2 = alphabet.getCharacters().subList(
											alphabet.getCharacters().size() / 2 + 1, alphabet.getCharacters().size());
									model.addAttribute("list1", list1);
									model.addAttribute("list2", list2);
									List<Question> ques = getRandom(lesson.getTests().get(0).getQuestions(), 10);
									model.addAttribute("ques", ques);
									session.setAttribute("testAlphabet", ques);
									AnswerList ansForm = new AnswerList();
									createObject(ansForm);
									model.addAttribute("form", ansForm);

								}

								model.addAttribute("course", course);
								model.addAttribute("lesson", lesson);

							} else {
								// la lesson2 tro di
								model.addAttribute("course", course);

								int pos = course.getLessons().indexOf(lesson);
								int preLessonId = course.getLessons().get(pos - 1).getId();
								Result preTestResult = checkResultHasTestInLesson(user, preLessonId);
								if (preTestResult != null) {
									int score = preTestResult.getScore();
									if (score >= 6) {
										if (courseId == 1 && lessonId < 3) {

											Alphabet alphabet = alphabetService.findById(lessonId);
											model.addAttribute("alphabet", alphabet);
											List<Character> list1 = alphabet.getCharacters().subList(0,
													alphabet.getCharacters().size() / 2);
											List<Character> list2 = alphabet.getCharacters().subList(
													alphabet.getCharacters().size() / 2 + 1,
													alphabet.getCharacters().size());
											model.addAttribute("list1", list1);
											model.addAttribute("list2", list2);
											List<Question> ques = getRandom(lesson.getTests().get(0).getQuestions(),
													10);
											model.addAttribute("ques", ques);
											session.setAttribute("testAlphabet", ques);

											AnswerList ansForm = new AnswerList();

											model.addAttribute("form", ansForm);

										}
										model.addAttribute("lesson", lesson);
									} else {

										Lesson lesson_ = lessonService.findById(preLessonId);

										if (courseId == 1 && lessonId < 3) {

											Alphabet alphabet = alphabetService.findById(lessonId - 1);
											model.addAttribute("alphabet", alphabet);
											List<Character> list1 = alphabet.getCharacters().subList(0,
													alphabet.getCharacters().size() / 2);
											List<Character> list2 = alphabet.getCharacters().subList(
													alphabet.getCharacters().size() / 2 + 1,
													alphabet.getCharacters().size());
											model.addAttribute("list1", list1);
											model.addAttribute("list2", list2);
											List<Question> ques = getRandom(lesson_.getTests().get(0).getQuestions(),
													10);
											model.addAttribute("ques", ques);
											session.setAttribute("testAlphabet", ques);

											AnswerList ansForm = new AnswerList();
											createObject(ansForm);
											model.addAttribute("form", ansForm);
										}
										model.addAttribute("lesson", lesson_);
									
										model.addAttribute("lowScore", "Bạn chưa đủ điểm để học bài tiếp theo");
									}
								} else {
									Lesson lesson_ = lessonService.findById(preLessonId);
									if (courseId == 1 && lessonId < 3) {

										Alphabet alphabet = alphabetService.findById(lessonId - 1);
										model.addAttribute("alphabet", alphabet);
										List<Character> list1 = alphabet.getCharacters().subList(0,
												alphabet.getCharacters().size() / 2);
										List<Character> list2 = alphabet.getCharacters().subList(
												alphabet.getCharacters().size() / 2 + 1,
												alphabet.getCharacters().size());
										model.addAttribute("list1", list1);
										model.addAttribute("list2", list2);
										List<Question> ques = getRandom(lesson_.getTests().get(0).getQuestions(), 10);
										model.addAttribute("ques", ques);
										session.setAttribute("testAlphabet", ques);
										AnswerList ansForm = new AnswerList();
										createObject(ansForm);
										model.addAttribute("form", ansForm);

									}
									model.addAttribute("lesson", lesson_);
									model.addAttribute("notTest", "Bạn chưa làm test ở bài học trước");
								}

							}

						}
						List<History> listHis = userService.loadHistory(userId, 2);
						History his = new History();
						his.setUser(user);
						his.setActivityType(2);// hoc
						his.setName("Học bài");
						his.setObjectId(lessonId);
						int check = 0;
						for (int i = 0; i < listHis.size(); i++) {
							if (listHis.get(i).getObjectId() == lessonId && listHis.get(i).getActivityType() == 2) {
								History h = historyService.updateTime(listHis.get(i));
								check = 1;
								break;
							}

						}
						if (check == 0)
							historyService.saveOrUpdate(his);

					}
				}
			} else {
				model.addAttribute("chuaLogin", "Chua login");
			}

		}

		return "views/web/lesson/index2";
	}

	private List<Question> getRandom(List<Question> list, int totalItems) {
		Random rand = new Random();

		List<Question> newList = new ArrayList<Question>();

		for (int i = 0; i < totalItems; i++) {

			int randomIndex = rand.nextInt(list.size());
			if (newList.contains(list.get(randomIndex)) == false)
				newList.add(list.get(randomIndex));
		}
		return newList;
	}

	private void createObject(AnswerList ansForm) {
		List<AnswerInfo> list = new ArrayList<AnswerInfo>();
		for (int i = 0; i < 10; i++) {
			list.add(new AnswerInfo());
		}
		ansForm.setAnswers(list);
	}

}
