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
		if (session.getAttribute("currentUser") == null) // chua dang nhap
		{
			logger.info("chua dang nhap");
			model.addAttribute("chuaLogin", "Mời login để học");

		} else {
			int userId = (int) session.getAttribute("currentUser");
			User user = userService.findById(userId);
			if(user!=null)
			{
				if(user.getLevel()==0)
				{
					// lam test
					model.addAttribute("lamTestLevel","Hãy làm test để biết cấp độ hiện tại. Sau đó bạn sẽ được học khóa học phù hợp!");
				}
				else {
					//hoc bai hoc dang hoc
					Lesson lesson = lessonService.findById(lessonId);
					Course course = lesson.getCourse();
					model.addAttribute("course", course);
					if (courseId == 1 && lessonId < 3) {

						Alphabet alphabet = alphabetService.findById(lessonId);
						model.addAttribute("alphabet", alphabet);
						List<Character> list1 = alphabet.getCharacters().subList(0, alphabet.getCharacters().size() / 2);
						List<Character> list2 = alphabet.getCharacters().subList(alphabet.getCharacters().size() / 2 + 1,
								alphabet.getCharacters().size());
						model.addAttribute("list1", list1);
						model.addAttribute("list2", list2);
						model.addAttribute("ques", getRandom(lesson.getTests().get(0).getQuestions(), 10));

					}
					model.addAttribute("lesson", lesson);
					
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
}
