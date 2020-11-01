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

import jlearning.model.Alphabet;
import jlearning.model.Blog;
import jlearning.model.Lesson;
import jlearning.model.Question;
import jlearning.model.Result;
import jlearning.model.Test;
import jlearning.model.User;
import jlearning.model.Character;
import jlearning.model.Course;
import jlearning.service.AlphabetService;
import jlearning.service.CourseService;
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

	@RequestMapping("/courses/{id1}/lessons/{id2}")
	public String index(@PathVariable("id1") int courseId, @PathVariable("id2") int lessonId, Model model,
			HttpServletRequest request) {
		checkObjectUser(model);
		HttpSession session = request.getSession();
		{
			int userId = (int) session.getAttribute("currentUser");
			User user = userService.findById(userId);
			if (user != null) {
				{
					// hoc bai hoc dang hoc
					Lesson lesson = lessonService.findById(lessonId);
					Course course = lesson.getCourse();
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
								model.addAttribute("ques", getRandom(lesson.getTests().get(0).getQuestions(), 10));

							}
							model.addAttribute("course", course);
							model.addAttribute("lesson", lesson);

						} else {
							// la lesson2 tro di
							model.addAttribute("course", course);
							int preLessonId = lessonId - 1;
							Result preTestResult = checkResultHasTestInLesson(user, preLessonId);
							if (preTestResult != null) {
								int score = preTestResult.getScore();
								if (score >= 6) {
									model.addAttribute("lesson", lesson);
								} else {
									Lesson lesson_ = lessonService.findById(lessonId-1);
									if (courseId == 1 && lessonId < 3) {

										Alphabet alphabet = alphabetService.findById(lessonId);
										model.addAttribute("alphabet", alphabet);
										List<Character> list1 = alphabet.getCharacters().subList(0,
												alphabet.getCharacters().size() / 2);
										List<Character> list2 = alphabet.getCharacters().subList(
												alphabet.getCharacters().size() / 2 + 1, alphabet.getCharacters().size());
										model.addAttribute("list1", list1);
										model.addAttribute("list2", list2);
										model.addAttribute("ques", getRandom(lesson.getTests().get(0).getQuestions(), 10));

									}
									model.addAttribute("lesson", lesson_);
									model.addAttribute("lowScore", "Bạn chưa đủ điểm để học bài tiếp theo");
								}
							} else {
								Lesson lesson_ = lessonService.findById(lessonId-1);
								if (courseId == 1 && lessonId < 3) {

									Alphabet alphabet = alphabetService.findById(lessonId);
									model.addAttribute("alphabet", alphabet);
									List<Character> list1 = alphabet.getCharacters().subList(0,
											alphabet.getCharacters().size() / 2);
									List<Character> list2 = alphabet.getCharacters().subList(
											alphabet.getCharacters().size() / 2 + 1, alphabet.getCharacters().size());
									model.addAttribute("list1", list1);
									model.addAttribute("list2", list2);
									model.addAttribute("ques", getRandom(lesson.getTests().get(0).getQuestions(), 10));

								}
								model.addAttribute("lesson", lesson_);
								model.addAttribute("notTest", "Bạn chưa làm test ở bài học trước");
							}

						}
					}

				}
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

	private Result checkResultHasTestInLesson(User user, int lessonId) {
		Result result = null;
		for (Result rs : user.getResults()) {

			if(rs.getTest()!=null)
			{
				if(rs.getTest().getLesson()!=null)
				{
					if (rs.getTest().getLesson().getId() == lessonId) {
						result = rs;

						break;
					}
				}
				
			}
			
		}
		return result;
	}
}
