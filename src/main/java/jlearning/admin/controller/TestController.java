package jlearning.admin.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jlearning.bean.QuestionInfo;
import jlearning.bean.TestInfo;
import jlearning.model.Course;
import jlearning.model.Test;
import jlearning.model.Test.Type;
import jlearning.service.LessonService;
import jlearning.service.TestService;

@Controller
@RequestMapping("/admin/tests")
public class TestController {
	private static final Logger logger = Logger.getLogger(TestController.class);
	@Autowired
	private TestService testService;
	@Autowired
	private LessonService lessonService;

	@GetMapping(value = { "", "/" })
	public String index(Model model) {
		List<Test> tests = testService.loadAllTest();
		model.addAttribute("tests", tests);
		return "views/admin/test/tests";
	}

	@GetMapping(value = "/{id}")
	public String show(Model model, @PathVariable("id") int id) {

		Test test = testService.findAndLoad(id);
		model.addAttribute("test", test);
		model.addAttribute("type", test.getType());
		return "views/admin/test/test";
	}

	@GetMapping(value = "/{id}/edit")
	public String edit(Model model, @PathVariable("id") int id) {
		Test test = testService.findById(id);
		model.addAttribute("testForm", test);
		List<Type> type = new ArrayList<Type>();
		type.add(Type.LEVEL);
		type.add(Type.EXAM);
		type.add(Type.LESSON);
		model.addAttribute("types", type);
		model.addAttribute("type", test.getType().toString());
		model.addAttribute("lessons", lessonService.loadAllLessons());
		model.addAttribute("status", "update");
		return "views/admin/test/test-form";
	}

	@GetMapping(value = "/add")
	public String addTest(Model model) {
		model.addAttribute("testForm", new TestInfo());
		List<String> type = new ArrayList<String>();
		type.add("LESSON");
		type.add("LEVEL");
		type.add("EXAM");
		model.addAttribute("types", type);
		model.addAttribute("lessons", lessonService.loadAllLessons());
		model.addAttribute("status", "add");
		return "views/admin/test/test-form";
	}

	@RequestMapping(value = "/new")
	public String saveTest(Model model, @ModelAttribute("testForm") TestInfo test, HttpServletRequest request,
			final RedirectAttributes redirectAttributes) {
		HttpSession session = request.getSession();
		if (session.getAttribute("questions") != null) {
			List<QuestionInfo> quesList = (List<QuestionInfo>) session.getAttribute("questions");
			testService.createTest(quesList, test);
			session.removeAttribute("questions");
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Tạo đề thành công");
		} else {
			testService.saveOrUpdate_(test);
			session.removeAttribute("questions");
		}

		return "redirect:/admin/tests";
	}
	@GetMapping(value = "{id}/delete")
	public String deleteTest(Model model, @PathVariable("id") int id,final RedirectAttributes redirectAttributes) {
		Test test = testService.findById(id);
		if(testService.delete(test)) {
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Xóa đề thành công");
		}else {
			redirectAttributes.addFlashAttribute("css", "error");
			redirectAttributes.addFlashAttribute("msg", "Xóa đề thất bại");
		}
		return "redirect:/admin/tests";
				
	}
	@GetMapping(value = "{id}/questions/{id2}/delete")
	public String deleteQues(Model model, @PathVariable("id") int id,@PathVariable("id2") int id2,final RedirectAttributes redirectAttributes) {
		Test test = testService.findById(id);
		if(testService.deleteQuestion(id2)) {
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Xóa câu hỏi thành công");
		}else {
			redirectAttributes.addFlashAttribute("css", "error");
			redirectAttributes.addFlashAttribute("msg", "Xóa câu hỏi thất bại");
		}
		return "redirect:/admin/tests/"+id;
				
	}
	
	@RequestMapping(value = "{id}/save")
	public String updateTest(Model model, @ModelAttribute("testForm") Test test, @PathVariable("id") int id,
			final RedirectAttributes redirectAttributes) {
		if (testService.saveOrUpdate(test) != null) {
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Sửa đề thành công");
		}
		else {
			redirectAttributes.addFlashAttribute("css", "error");
			redirectAttributes.addFlashAttribute("msg", "Sửa đề thất bại");
		}
		return "redirect:/admin/tests/" + id;
	}

	@GetMapping(value = "/{id}/addQuestionManual")
	public String addQuestion(Model model, @PathVariable("id") int id, HttpServletRequest request) {
		HttpSession session = request.getSession();
		model.addAttribute("status", "add");
		model.addAttribute("quesForm", new QuestionInfo());
		session.setAttribute("testId", id); // session
		return "views/admin/test/newQuestionManual";
	}

	@GetMapping(value = "/{id}/addQuestion")
	public String addQuestionF(Model model, @PathVariable("id") int id, HttpServletRequest request) {
		HttpSession session = request.getSession();
		model.addAttribute("status", "add");
		model.addAttribute("quesForm", new QuestionInfo());
		model.addAttribute("testId", id);
		return "views/admin/test/newQuestion";
	}

	@GetMapping(value = "/addQuestionManual") // test Moi hoan toan
	public String addQuestion2(Model model) {
		model.addAttribute("status", "add");

		model.addAttribute("quesForm", new QuestionInfo());
		return "views/admin/test/newQuestionManual";
	}

	@RequestMapping(value = "/questions/new")
	public String saveQuestion(Model model, @ModelAttribute("quesForm") QuestionInfo questionInfo,
			HttpServletRequest request) {

		for (int i = 0; i < 4; i++) {
			if (request.getParameter("ans").compareTo(Integer.toString(i)) == 0) {
				questionInfo.getAnsList().get(i).setIsTrue(1);
			}

		}
		HttpSession session = request.getSession();
		List<QuestionInfo> questions = new ArrayList<>();
		if (session.getAttribute("questions") != null) {
			questions = (List<QuestionInfo>) session.getAttribute("questions");
		}
		questions.add(questionInfo);
		session.setAttribute("questions", questions);

		if (session.getAttribute("testId") != null) {
			int testId = Integer.parseInt(session.getAttribute("testId").toString());
			testService.createQuestion(questionInfo, testId);
			session.removeAttribute("questions");
			session.removeAttribute("testId");
			return "redirect:/admin/tests/" + testId;
		}
		return "redirect:/admin/tests/add";
	}

	@GetMapping(value = "/{id}/addQuestionFile")
	public String addQuestion3(Model model, @PathVariable("id") int id) {
		model.addAttribute("status", "add");
		model.addAttribute("quesForm", new QuestionInfo());

		return "views/admin/test/newQuestionManual";
	}

	@GetMapping(value = "/addQuestionFile")
	public String addQuestion4(Model model, @PathVariable("id") int id) {
		model.addAttribute("status", "add");
		model.addAttribute("quesForm", new QuestionInfo());
		return "views/admin/test/newQuestionManual";
	}
}
