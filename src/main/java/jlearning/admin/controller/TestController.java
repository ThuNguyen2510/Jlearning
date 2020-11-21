package jlearning.admin.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mchange.v2.cfg.PropertiesConfigSource.Parse;

import jlearning.bean.AnswerInfo;
import jlearning.bean.AnswerInfo_;
import jlearning.bean.QuestionInfo;
import jlearning.bean.TestInfo;
import jlearning.bean.UserInfo;
import jlearning.helper.ExcelHelper;
import jlearning.model.Course;
import jlearning.model.Question;
import jlearning.model.Question.Part;
import jlearning.model.Test;
import jlearning.model.Test.Type;
import jlearning.service.LessonService;
import jlearning.service.QuestionService;
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
		Test test = testService.findAndLoad(id);
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
	public String deleteTest(Model model, @PathVariable("id") int id, final RedirectAttributes redirectAttributes) {
		Test test = testService.findById(id);
		if (testService.delete(test)) {
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Xóa đề thành công");
		} else {
			redirectAttributes.addFlashAttribute("css", "error");
			redirectAttributes.addFlashAttribute("msg", "Xóa đề thất bại");
		}
		return "redirect:/admin/tests";

	}

	@GetMapping(value = "{id}/questions/{id2}/delete")
	public String deleteQues(Model model, @PathVariable("id") int id, @PathVariable("id2") int id2,
			final RedirectAttributes redirectAttributes) {
		Test test = testService.findById(id);
		if (testService.deleteQuestion(id2)) {
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Xóa câu hỏi thành công");
		} else {
			redirectAttributes.addFlashAttribute("css", "error");
			redirectAttributes.addFlashAttribute("msg", "Xóa câu hỏi thất bại");
		}
		return "redirect:/admin/tests/" + id;

	}

	@GetMapping(value = "/{id2}/deleteQuesSession")
	public String deleteQuesSession(Model model,@PathVariable("id2") int id2,
			HttpServletRequest request, final RedirectAttributes redirectAttributes) {
		List<Question> questions = new ArrayList<>();
		HttpSession session = request.getSession();
		if (session.getAttribute("questions") != null) {
			questions = (List<Question>) session.getAttribute("questions");
		}
		if (questions.get(id2) != null) {
			questions.remove(id2);
			session.setAttribute("questions", questions);
			model.addAttribute("status","add");
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Xóa câu hỏi thành công");

		} else {
			redirectAttributes.addFlashAttribute("css", "error");
			redirectAttributes.addFlashAttribute("msg", "Xóa câu hỏi thất bại");
		}
		return "redirect:/admin/tests/add";

	}

	@RequestMapping(value = "{id}/questions/{id2}/save")
	public String updateQues(Model model, @PathVariable("id") int id, @PathVariable("id2") int id2,
			HttpServletRequest request, final RedirectAttributes redirectAttributes,
			@ModelAttribute("quesForm") Question question) {
		String typeCss = "";
		String message = "";
		for (int i = 0; i < 4; i++) {
			if (request.getParameter("ans").compareTo(Integer.toString(i)) == 0) {
				logger.info(request.getParameter("ans"));
				question.getAnswers().get(i).setIsTrue(1);
				
			}
		}
		if (testService.updateQuestion(question) != null) {
			typeCss = "success";
			message = "Sửa thành công!!";
		}
		redirectAttributes.addFlashAttribute("css", typeCss);
		redirectAttributes.addFlashAttribute("msg", message);
		return "redirect:/admin/tests/" + id;
	}

	@GetMapping(value = "/questions/{id2}/edit")
	public String editQues(Model model, @PathVariable("id2") int id2, final RedirectAttributes redirectAttributes) {
		model.addAttribute("status", "update");
		List<Part> list = new ArrayList<Part>();
		list.add(Part.vocab);
		list.add(Part.listen);
		list.add(Part.gram);
		list.add(Part.read);
		model.addAttribute("parts", list);
		model.addAttribute("quesForm", testService.getQuestion(id2));
		return "views/admin/test/newQuestionManual";
	}

	@RequestMapping(value = "{id}/save")
	public String updateTest(Model model, @ModelAttribute("testForm") Test test, @PathVariable("id") int id,
			final RedirectAttributes redirectAttributes) {
		if (testService.saveOrUpdate(test) != null) {
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Sửa đề thành công");
		} else {
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
	public String saveQuestion(Model model, final RedirectAttributes redirectAttributes,@ModelAttribute("quesForm") QuestionInfo questionInfo,
			HttpServletRequest request) {
		String typeCss = "";
		String message = "";
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
		typeCss = "success";
		message = "Tạo thành công!!";

		if (session.getAttribute("testId") != null) {
			int testId = Integer.parseInt(session.getAttribute("testId").toString());
			testService.createQuestion(questionInfo, testId);
			typeCss = "success";
			message = "Tạo thành công!!";
			session.removeAttribute("questions");
			session.removeAttribute("testId");
			redirectAttributes.addFlashAttribute("css", typeCss);
			redirectAttributes.addFlashAttribute("msg", message);
			return "redirect:/admin/tests/" + testId;
		}
		redirectAttributes.addFlashAttribute("css", typeCss);
		redirectAttributes.addFlashAttribute("msg", message);
		return "redirect:/admin/tests/add";
	}

	@GetMapping(value = "/{id}/addQuestionFile")
	public String addQuestion3(Model model, @PathVariable("id") int id) throws Exception {
		model.addAttribute("status", "add");
		model.addAttribute("testId", id);
		return "views/admin/test/newQuestionFile";
	}

	@RequestMapping(value = "{id}/process")
	public String importFile(@RequestParam("file") MultipartFile excelfile, @PathVariable("id") int testId, Model model)
			throws Exception {
		List<QuestionInfo> questions = new ArrayList<>();
		int i = 0;
		// Creates a workbook object from the uploaded excelfile
		XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
		// Creates a worksheet object representing the first sheet
		XSSFSheet worksheet = workbook.getSheetAt(0);
		// Reads the data in excel file until last row is encountered
		while (i <= worksheet.getLastRowNum()) {
			// Creates an object for the UserInfo Model
			QuestionInfo ques = new QuestionInfo();
			// Creates an object representing a single row in excel
			XSSFRow row = worksheet.getRow(i++);
			// Sets the Read data to the model class
			if(row.getCell(0)!=null && row.getCell(0).getCellType()!= Cell.CELL_TYPE_BLANK )ques.setContent(row.getCell(0).getStringCellValue());
			if(row.getCell(1)!=null && row.getCell(1).getCellType()!= Cell.CELL_TYPE_BLANK )ques.setLevel((int) (row.getCell(1).getNumericCellValue()));
			else ques.setLevel(0);
			if(row.getCell(2)!=null && row.getCell(2).getCellType()!= Cell.CELL_TYPE_BLANK )ques.setPart((int) (row.getCell(2).getNumericCellValue()));
			else ques.setPart(1);
			int sizeAns = (int) row.getCell(3).getNumericCellValue();
			int k = 4, g = 5;
			List<AnswerInfo_> list = new ArrayList<>();
			for (int j = 1; j <= sizeAns; j++) {
				AnswerInfo_ ans = new AnswerInfo_();
				if(row.getCell(k)!=null && row.getCell(k).getCellType()!= Cell.CELL_TYPE_BLANK )ans.setContent(row.getCell(k).getStringCellValue());
				if(row.getCell(g)!=null && row.getCell(g).getCellType()!= Cell.CELL_TYPE_BLANK )ans.setIsTrue((int) (row.getCell(g).getNumericCellValue()));
				k += 2;
				g += 2;
				list.add(ans);

			}
			ques.setAnsList(list);
			testService.createQuestion(ques, testId);

		}

		return "redirect:/admin/tests/" + testId;
	}

	@GetMapping(value = "/addQuestionFile")
	public String addQuestion4(Model model) {
		model.addAttribute("status", "add1");
		model.addAttribute("quesForm", new QuestionInfo());
		return "views/admin/test/newQuestionFile";
	}

	@RequestMapping(value = "/process")
	public String importFile2(@RequestParam("file") MultipartFile excelfile, Model model, HttpServletRequest request)
			throws Exception {
		
		List<QuestionInfo> questions = new ArrayList<>();
		HttpSession session = request.getSession();
		if (session.getAttribute("questions") != null) {
			questions = (List<QuestionInfo>) session.getAttribute("questions");
		}
		int i = 0;
		// Creates a workbook object from the uploaded excelfile
		XSSFWorkbook workbook = new XSSFWorkbook(excelfile.getInputStream());
		// Creates a worksheet object representing the first sheet
		XSSFSheet worksheet = workbook.getSheetAt(0);
		// Reads the data in excel file until last row is encountered
		while (i <= worksheet.getLastRowNum()) {
			// Creates an object for the UserInfo Model
			QuestionInfo ques = new QuestionInfo();
			// Creates an object representing a single row in excel
			XSSFRow row = worksheet.getRow(i++);
			// Sets the Read data to the model class
			ques.setContent(row.getCell(0).getStringCellValue());
			ques.setLevel((int) (row.getCell(1).getNumericCellValue()));
			ques.setPart((int) (row.getCell(2).getNumericCellValue()));
			int sizeAns = (int) row.getCell(3).getNumericCellValue();
			int k = 4, g = 5;
			List<AnswerInfo_> list = new ArrayList<>();
			for (int j = 1; j <= sizeAns; j++) {
				AnswerInfo_ ans = new AnswerInfo_();
				ans.setContent(row.getCell(k).getStringCellValue());
				ans.setIsTrue((int) (row.getCell(g).getNumericCellValue()));
				k += 2;
				g += 2;
				list.add(ans);

			}
			ques.setAnsList(list);
			questions.add(ques);
		}
		session.setAttribute("questions", questions);
		return "redirect:/admin/tests/add";
	}
}
