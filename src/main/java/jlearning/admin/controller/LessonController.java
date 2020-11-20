package jlearning.admin.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jlearning.bean.ListenInfo;
import jlearning.bean.MyFile;
import jlearning.bean.GramInfo;
import jlearning.bean.LessonInfo;
import jlearning.bean.VocabInfo;
import jlearning.model.Alphabet;
import jlearning.model.Course;
import jlearning.model.Grammar;
import jlearning.model.Lesson;
import jlearning.model.Listening;
import jlearning.model.Vocabulary;
import jlearning.service.AlphabetService;
import jlearning.service.CourseService;
import jlearning.service.GrammarService;
import jlearning.service.LessonService;

import jlearning.service.ListeningService;
import jlearning.service.VocabularyService;

@Controller
@RequestMapping("/admin/lessons")
public class LessonController {
	private static final Logger logger = Logger.getLogger(LessonController.class);

	@Autowired
	private LessonService lessonService;

	@Autowired
	private AlphabetService alphabetService;

	@Autowired
	private CourseService courseService;

	@Autowired
	private VocabularyService vocabularyService;

	@Autowired
	private GrammarService grammarService;

	@Autowired
	private ListeningService listeningService;

	@GetMapping(value = { "", "/" })
	public String index(Model model) {
		List<Lesson> lessons = lessonService.loadAllLessons();
		model.addAttribute("lessons", lessons);
		return "views/admin/lesson/lessons";
	}

	@GetMapping(value = "/{id}")
	public String show(Model model, @PathVariable("id") int id) {
		Lesson lesson = lessonService.findById(id);
		if (id < 3) {
			model.addAttribute("alphabet", "alphabet");
			Alphabet alphabet = alphabetService.findById(id);
			model.addAttribute("alphabet", alphabet);
		}
		model.addAttribute("lesson", lesson);
		return "views/admin/lesson/lesson";
	}

	@GetMapping(value = "/{id}/delete")
	public String delete(Model model, @PathVariable("id") int id, final RedirectAttributes redirectAttributes) {
		Lesson lesson = lessonService.findById(id);
		if (lessonService.delete(lesson)) {
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Xóa bài học thành công");
		} else {
			redirectAttributes.addFlashAttribute("css", "error");
			redirectAttributes.addFlashAttribute("msg", "Xóa bài học thất bài");
		}
		return "redirect:/admin/lessons";
	}

	@RequestMapping(value = "/add")
	public String add(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Lesson lesson = new Lesson();
		model.addAttribute("lessonForm", lesson);
		List<Course> courses = courseService.loadCourses();
		model.addAttribute("courses", courses);
		model.addAttribute("status", "add1");
		return "views/admin/lesson/newLessonManual";
	}

	@RequestMapping(value = "/addNormal")
	public String addManual(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Lesson lesson = new Lesson();

		model.addAttribute("lessonForm", lesson);
		List<Course> courses = courseService.loadCourses();
		model.addAttribute("courses", courses);

		model.addAttribute("courseId", session.getAttribute("courseId"));
		model.addAttribute("status", "add");
		if (session.getAttribute("courseId").toString().compareTo("0") != 0) {
			String courseName = courseService.findById(Integer.parseInt(session.getAttribute("courseId").toString()))
					.getName();
			model.addAttribute("courseName", courseName);
		}
		return "views/admin/lesson/newLessonManual";
	}

	@RequestMapping(value = "/addImport")
	public String addImport(Model model) {
		Lesson course = new Lesson();
		model.addAttribute("lessonForm", course);
		model.addAttribute("status", "add");

		return "views/admin/lesson/newLessonImport";
	}

	@RequestMapping(value = "/new")
	public String newLesson(@ModelAttribute("lessonForm") Lesson lesson, BindingResult result, Model model,
			final RedirectAttributes redirectAttributes, HttpServletRequest request) {
		String typeCss = "error";
		String message = "Input sai!";
		HttpSession session = request.getSession();
		LessonInfo lf = new LessonInfo();
		lf.setName(lesson.getName());
		lf.setDescription(lesson.getDescription());
		if (session.getAttribute("vocabs") != null) {
			lf.setVocabs((List<VocabInfo>) session.getAttribute("vocabs"));
		}
		if (session.getAttribute("grams") != null) {
			lf.setGrams((List<GramInfo>) session.getAttribute("grams"));
		}
		if (session.getAttribute("listens") != null) {
			lf.setListens((List<ListenInfo>) session.getAttribute("listens"));
		}
		List<LessonInfo> list = new ArrayList<LessonInfo>();
		if (session.getAttribute("newLesson") != null) {
			list = (List<LessonInfo>) session.getAttribute("newLesson");
		}
		list.add(lf);
		session.setAttribute("newLesson", list);
		logger.info("COURSEID" + session.getAttribute("courseId"));
		logger.info("PARAM " + request.getParameter("course"));
		session.removeAttribute("newCourse");
		if (session.getAttribute("courseId") != null) {
			if (session.getAttribute("courseId").toString().compareTo("0") != 0) {
				// lesson.setCourse(courseService.findById(Integer.parseInt(request.getParameter("course"))));
				if (session.getAttribute("courseId") != null) // có course
					session.removeAttribute("courseId");
				/*
				 * if (lessonService.saveOrUpdate(lesson) == null) {
				 */
				if (lessonService.createLessonWithListContent(lf,
						Integer.parseInt(request.getParameter("course"))) == null) {
					redirectAttributes.addFlashAttribute("css", typeCss);
					redirectAttributes.addFlashAttribute("msg", message);
					return "redirect:/admin/lessons";
				} else {
					typeCss = "success";
					message = "Sửa/Tạo bài học thành công!!";
					session.removeAttribute("vocabs");
					session.removeAttribute("grams");
					session.removeAttribute("listens");
					session.removeAttribute("courseId");
					session.removeAttribute("newLesson");
					session.removeAttribute("newCourse");
					redirectAttributes.addFlashAttribute("css", typeCss);
					redirectAttributes.addFlashAttribute("msg", message);
					return "redirect:/admin/lessons/" + lesson.getId();
				}
			} else {
				// chua co course
				return "redirect:/admin/courses/add";
			}

		} else if (request.getParameter("course") != null) { // select course
			// lesson.setCourse(courseService.findById(Integer.parseInt(request.getParameter("course"))));
			if (lessonService.createLessonWithListContent(lf,
					Integer.parseInt(request.getParameter("course"))) == null) {
				redirectAttributes.addFlashAttribute("css", typeCss);
				redirectAttributes.addFlashAttribute("msg", message);
				return "redirect:/admin/lessons";
			} else {
				typeCss = "success";
				message = "Sửa/Tạo bài học thành công!!";
				session.removeAttribute("vocabs");
				session.removeAttribute("grams");
				session.removeAttribute("listens");
				session.removeAttribute("courseId");
				session.removeAttribute("newLesson");
				session.removeAttribute("newCourse");
				redirectAttributes.addFlashAttribute("css", typeCss);
				redirectAttributes.addFlashAttribute("msg", message);
				return "redirect:/admin/lessons/" + lessonService
						.createLessonWithListContent(lf, Integer.parseInt(request.getParameter("course"))).getId();
			}

		}

		return "redirect:/admin/lessons/" + lesson.getId();

	}

	@RequestMapping(value = "/update")
	public String Update(@ModelAttribute("lessonForm") Lesson lesson, BindingResult result, Model model,
			final RedirectAttributes redirectAttributes, HttpServletRequest request) {
		String typeCss = "error";
		String message = "Input sai!";
		Lesson old = lessonService.findById(lesson.getId());
		HttpSession session = request.getSession();
		lesson.setCourse(old.getCourse());
		if (lessonService.saveOrUpdate(lesson) == null) {
			redirectAttributes.addFlashAttribute("css", typeCss);
			redirectAttributes.addFlashAttribute("msg", message);
			return "redirect:/admin/lessons";
		} else {
			typeCss = "success";
			message = "Sửa bài học thành công!!";
			session.removeAttribute("vocabs");
			session.removeAttribute("grams");
			session.removeAttribute("listens");
			session.removeAttribute("courseId");
			session.removeAttribute("newLesson");
			redirectAttributes.addFlashAttribute("css", typeCss);
			redirectAttributes.addFlashAttribute("msg", message);
			return "redirect:/admin/lessons/" + lesson.getId();
		}

	}

	@GetMapping(value = "/{index}/lessonSession")
	public String showLessonInSesson(Model model, HttpServletRequest request, @PathVariable("index") int id) {
		HttpSession session = request.getSession();
		List<LessonInfo> list = (List<LessonInfo>) session.getAttribute("newLesson");
		LessonInfo lesson = list.get(id);
		model.addAttribute("lesson", lesson);
		model.addAttribute("pos", id);
		model.addAttribute("inSession", "session");
		return "views/admin/lesson/lessonSession";
	}

	@GetMapping(value = "/{index}/editLessonSession")
	public String editInSession(Model model, @PathVariable("id") int id) {

		return "views/admin/lesson/lessonSession";
	}

	@GetMapping(value = "/{id}/deleteLessonSession")
	public String deleteInSession(Model model, @PathVariable("id") int id, HttpServletRequest request) {
		HttpSession session = request.getSession();
		List<LessonInfo> list = (List<LessonInfo>) session.getAttribute("newLesson");
		list.remove(id);
		session.setAttribute("newLesson", list);
		return "redirect:/admin/courses/add";
	}

	@GetMapping("/{id}/edit")
	public String editCourse(@PathVariable("id") int id, Model model, final RedirectAttributes redirectAttributes) {
		String typeCss = "error";
		String message = "Lesson not found!";
		Lesson lesson = lessonService.findById(id);
		if (lesson != null) {
			model.addAttribute("lessonForm", lesson);
			model.addAttribute("status", "update");
			return "views/admin/lesson/lesson-form";
		}
		redirectAttributes.addFlashAttribute("css", typeCss);
		redirectAttributes.addFlashAttribute("msg", message);
		return "redirect:/admin/lessons";

	}

	@GetMapping("/{id}/vocabs") // show-edit-delete
	public String vocabs(@PathVariable("id") int id, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (lessonService.findById(id) != null) {
			if (id < 3) {
				model.addAttribute("alphabet", "alphabet");
				Alphabet alphabet = alphabetService.findById(id);
				model.addAttribute("alphabet", alphabet);
			} else {
				List<Vocabulary> vocabs = lessonService.findById(id).getVocabularies();
				model.addAttribute("vocabs", vocabs);
				model.addAttribute("lessonId", id);

			}

		} else {
			model.addAttribute("vocabForm", new Vocabulary());
		}
		model.addAttribute("lessonId", id);
		session.setAttribute("lessonId", id);
		return "views/admin/lesson/vocab";
	}

	@GetMapping("/{id}/vocab/add") // show-edit-delete
	public String vocabAdd(@PathVariable("id") int id, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("lessonId", id);
		return "views/admin/lesson/vocab-form";
	}

	@RequestMapping(value = "/addVocabNormal")
	public String addVocabManual(Model model, HttpServletRequest request) {
		// if id!=0 thi save ; ko thi luu vao session
		HttpSession session = request.getSession();
		model.addAttribute("myAudio", new MyFile());
		model.addAttribute("status", "add");
		model.addAttribute("lessonId", session.getAttribute("lessonId"));
		model.addAttribute("vocabForm", new VocabInfo());
		return "views/admin/lesson/newVocabNormal";
	}

	@RequestMapping(value = "/vocabs/add")
	public String saveVocab(Model model, @ModelAttribute("vocabForm") VocabInfo vocab,
			final RedirectAttributes redirectAttributes, HttpServletRequest request,
			@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
		HttpSession session = request.getSession();
		// if id!=0 thi save ; ko thi luu vao session
		String typeCss = "";
		String message = "";
		int lessonId = 0;
		File fileNew = new File("C:/Users/nguye/eclipse-workspace/Jlearning/src/main/webapp/assets/upload",
				file.getOriginalFilename());
		file.transferTo(fileNew);

		vocab.setAudio("/haru/assets/upload/" + file.getOriginalFilename());
		if (session.getAttribute("lessonId") != null) {
			lessonId = Integer.parseInt(session.getAttribute("lessonId").toString());
		}
		if (lessonId != 0) {
			lessonService.createVocab(vocab, lessonId);
			typeCss = "success";
			message = "Thêm thành công!!";

		} else {

			List<VocabInfo> vocabs = new ArrayList<VocabInfo>();
			if (session.getAttribute("vocabs") != null) {
				vocabs = (List<VocabInfo>) session.getAttribute("vocabs");
			}
			vocabs.add(vocab);
			session.setAttribute("vocabs", vocabs);
			typeCss = "success";
			message = "Thêm thành công!!";

		}
		redirectAttributes.addFlashAttribute("css", typeCss);
		redirectAttributes.addFlashAttribute("msg", message);

		return "redirect:/admin/lessons/" + session.getAttribute("lessonId") + "/vocabs";

	}

	@RequestMapping(value = "/{id}/vocabs/{id2}/delete")
	public String vocabdelete(@PathVariable("id") int id, @PathVariable("id2") int id2, Model model,
			final RedirectAttributes redirectAttributes, HttpServletRequest request) {
		String typeCss = "";
		String message = "";
		if (id == 0) {
			HttpSession session = request.getSession();
			List<VocabInfo> list = (List<VocabInfo>) session.getAttribute("vocabs");
			list.remove(id2);
			session.setAttribute("vocabs", list);
			if (list.size() == 0)
				session.removeAttribute("vocabs");
			typeCss = "success";
			message = "Xoá thành công!!";

		} else {
			if (lessonService.deleteVocab(id2)) {
				typeCss = "success";
				message = "Xoá thành công!!";

			} else {
				typeCss = "error";
				message = "Xoá thất bại!!";
			}
		}

		redirectAttributes.addFlashAttribute("css", typeCss);
		redirectAttributes.addFlashAttribute("msg", message);

		return "redirect:/admin/lessons/" + id + "/vocabs";
	}

	@RequestMapping(value = "/{id}/vocabs/{id2}/edit")
	public String vocabEdit(@PathVariable("id") int id, @PathVariable("id2") int id2, Model model,
			final RedirectAttributes redirectAttributes) {

		Vocabulary vocab = vocabularyService.findById(id2);
		model.addAttribute("vocabForm", vocab);
		return "views/admin/lesson/editVocab";
	}

	@RequestMapping(value = "/{id}/vocabs/{id2}/save")
	public String vocabSave(@PathVariable("id") int id, @PathVariable("id2") int id2, Model model,
			final RedirectAttributes redirectAttributes, @ModelAttribute("vocabForm") Vocabulary vocab,
			@RequestParam(value = "file", required = false) MultipartFile file)
			throws IllegalStateException, IOException {
		String typeCss = "";
		String message = "";

		if (file.isEmpty() != true) {
			File fileNew = new File("C:/Users/nguye/eclipse-workspace/Jlearning/src/main/webapp/assets/upload",
					file.getOriginalFilename());
			file.transferTo(fileNew);
			vocab.setAudio("/haru/assets/upload/" + file.getOriginalFilename());
		}

		if (vocabularyService.saveOrUpdate(vocab) != null) {
			typeCss = "success";
			message = "Sửa thành công!!";

		} else {
			typeCss = "error";
			message = "Sửa thất bại!!";
		}
		redirectAttributes.addFlashAttribute("css", typeCss);
		redirectAttributes.addFlashAttribute("msg", message);
		return "redirect:/admin/lessons/" + id;
	}

	@GetMapping("/{id}/grams")
	public String grams(@PathVariable("id") int id, Model model) {
		if (lessonService.findById(id) != null) {
			List<Grammar> grams = lessonService.findById(id).getGrammars();
			model.addAttribute("grams", grams);
			model.addAttribute("lessonId", id);

		} else {
			model.addAttribute("gramForm", new Grammar());
		}
		model.addAttribute("lessonId", id);
		return "views/admin/lesson/gram";
	}

	@GetMapping("/{id}/grams/{id2}/edit")
	public String gramEdit(@PathVariable("id") int id, @PathVariable("id2") int id2, Model model) {
		model.addAttribute("gramForm", lessonService.getGram(id2));
		model.addAttribute("status", "update");
		return "views/admin/lesson/newGramNormal";
	}

	@RequestMapping("{id2}/grams/{id}/save")
	public String gramEdit(HttpServletRequest request, @PathVariable("id") int id, @PathVariable("id2") int id2,
			Model model, @ModelAttribute("gramForm") Grammar gram, final RedirectAttributes redirectAttributes) {
		String typeCss = "";
		String message = "";

		if (grammarService.saveOrUpdate(gram) != null) {
			typeCss = "success";
			message = "Sửa thành công!!";
		} else {
			typeCss = "error";
			message = "Sửa thất bại!!";
		}
		redirectAttributes.addFlashAttribute("css", typeCss);
		redirectAttributes.addFlashAttribute("msg", message);
		return "redirect:/admin/lessons/" + id2 + "/grams";
	}

	@GetMapping("/{id}/grams/{id2}/delete")
	public String gramdelete(@PathVariable("id") int id, @PathVariable("id2") int id2, Model model,
			final RedirectAttributes redirectAttributes) {
		String typeCss = "";
		String message = "";
		if (lessonService.deleteGram(id2)) {
			typeCss = "success";
			message = "Xoá thành công!!";

		} else {
			typeCss = "fail";
			message = "Xoá thất bại!!";
		}
		redirectAttributes.addFlashAttribute("css", typeCss);
		redirectAttributes.addFlashAttribute("msg", message);

		return "redirect:/admin/lessons/" + id + "/grams";
	}

	@GetMapping("/{id}/gram/add") // show-edit-delete
	public String gramAdd(@PathVariable("id") int id, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		model.addAttribute("lessonId", id);
		session.setAttribute("lessonId", id);
		return "views/admin/lesson/gram-form";
	}

	@RequestMapping(value = "/addGramNormal")
	public String addGramManual(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		model.addAttribute("status", "add");
		model.addAttribute("lessonId", session.getAttribute("lessonId"));
		model.addAttribute("gramForm", new GramInfo());
		return "views/admin/lesson/newGramNormal";
	}

	@RequestMapping(value = "/grams/add")
	public String saveGram(Model model, @ModelAttribute("gramForm") GramInfo gram, HttpServletRequest request) {
		HttpSession session = request.getSession();
		// if id!=0 thi save ; ko thi luu vao session
		int lessonId = 0;
		if (session.getAttribute("lessonId") != null) {
			lessonId = Integer.parseInt(session.getAttribute("lessonId").toString());
		}
		if (lessonId != 0) {
			lessonService.createGram(gram, lessonId);

		} else {

			List<GramInfo> grams = new ArrayList<GramInfo>();
			if (session.getAttribute("grams") != null) {
				grams = (List<GramInfo>) session.getAttribute("grams");
			}
			grams.add(gram);
			session.setAttribute("grams", grams);

		}
		return "redirect:/admin/lessons/" + session.getAttribute("lessonId") + "/grams";

	}

	@GetMapping("/{id}/listens") // show-edit-delete
	public String listens(@PathVariable("id") int id, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (lessonService.findById(id) != null) {
			List<Listening> listens = lessonService.findById(id).getListenings();
			model.addAttribute("listens", listens);
			model.addAttribute("lessonId", id);

		} else {
			model.addAttribute("listenForm", new Vocabulary());
		}
		model.addAttribute("lessonId", id);
		session.setAttribute("lessonId", id);
		return "views/admin/lesson/listen";
	}

	@GetMapping("/{id}/listen/add")
	public String listenAdd(@PathVariable("id") int id, Model model) {
		model.addAttribute("lessonId", id);
		return "views/admin/lesson/listen-form";
	}

	@RequestMapping(value = "/addListenNormal")
	public String addListenManual(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		model.addAttribute("status", "add");
		model.addAttribute("lessonId", session.getAttribute("lessonId"));
		model.addAttribute("listenForm", new ListenInfo());
		return "views/admin/lesson/newListenNormal";
	}

	@RequestMapping(value = "/listens/add")
	public String saveListen(Model model, @ModelAttribute("listenForm") ListenInfo listen, HttpServletRequest request,
			@RequestParam(value = "fileAudio") MultipartFile fileAudio,final RedirectAttributes redirectAttributes,
			@RequestParam(value = "fileImage", required = false) MultipartFile fileImage)
			throws IllegalStateException, IOException {
		String typeCss = "";
		String message = "";
		if (fileAudio.isEmpty() != true) {
			File fileNew = new File("C:/Users/nguye/eclipse-workspace/Jlearning/src/main/webapp/assets/upload",
					fileAudio.getOriginalFilename());
			fileAudio.transferTo(fileNew);
			listen.setAudio("/haru/assets/upload/" + fileAudio.getOriginalFilename());
		}
		if (fileImage.isEmpty() != true) {
			File fileNew = new File("C:/Users/nguye/eclipse-workspace/Jlearning/src/main/webapp/assets/upload",
					fileImage.getOriginalFilename());
			fileImage.transferTo(fileNew);
			listen.setImage("/haru/assets/upload/" + fileImage.getOriginalFilename());
		}

		HttpSession session = request.getSession();
		// if id!=0 thi save ; ko thi luu vao session
		int lessonId = 0;
		if (session.getAttribute("lessonId") != null) {
			lessonId = Integer.parseInt(session.getAttribute("lessonId").toString());
		}
		if (lessonId != 0) {
			lessonService.createListen(listen, lessonId);
			typeCss = "success";
			message = "Thêm thành công!!";

		} else {

			List<ListenInfo> listens = new ArrayList<ListenInfo>();
			if (session.getAttribute("listens") != null) {
				listens = (List<ListenInfo>) session.getAttribute("listens");
			}
			listens.add(listen);
			session.setAttribute("listens", listens);
			typeCss = "success";
			message = "Thêm thành công!!";

		}
		redirectAttributes.addFlashAttribute("css", typeCss);
		redirectAttributes.addFlashAttribute("msg", message);
		
		return "redirect:/admin/lessons/" + session.getAttribute("lessonId") + "/listens";

	}

	@GetMapping("/{id}/listens/{id2}/delete")
	public String listendelete(@PathVariable("id") int id, @PathVariable("id2") int id2, Model model,
			final RedirectAttributes redirectAttributes) {
		String typeCss = "";
		String message = "";
		if (lessonService.deleteListen(id2)) {
			typeCss = "success";
			message = "Xoá thành công!!";

		} else {
			typeCss = "fail";
			message = "Xoá thất bại!!";
		}
		redirectAttributes.addFlashAttribute("css", typeCss);
		redirectAttributes.addFlashAttribute("msg", message);

		return "redirect:/admin/lessons/" + id + "/listens";
	}

	@GetMapping("/{id}/listens/{id2}/edit")
	public String listeEdit(@PathVariable("id") int id, @PathVariable("id2") int id2, Model model,
			final RedirectAttributes redirectAttributes) {
		model.addAttribute("status", "update");
		model.addAttribute("listenForm", lessonService.getListen(id2));

		return "views/admin/lesson/newListenNormal";
	}

	@RequestMapping("/{id}/listens/{id2}/save")
	public String listenSave(@PathVariable("id") int id, @PathVariable("id2") int id2, Model model,
			final RedirectAttributes redirectAttributes, @ModelAttribute("listenForm") Listening listen,
			@RequestParam(value = "fileAudio") MultipartFile fileAudio,
			@RequestParam(value = "fileImage", required = false) MultipartFile fileImage)
			throws IllegalStateException, IOException {
		String typeCss = "";
		String message = "";
		if (fileAudio.isEmpty() != true) {
			File fileNew = new File("C:/Users/nguye/eclipse-workspace/Jlearning/src/main/webapp/assets/upload",
					fileAudio.getOriginalFilename());
			fileAudio.transferTo(fileNew);
			listen.setAudio("/haru/assets/upload/" + fileAudio.getOriginalFilename());
		}
		if (fileImage.isEmpty() != true) {
			File fileNew = new File("C:/Users/nguye/eclipse-workspace/Jlearning/src/main/webapp/assets/upload",
					fileImage.getOriginalFilename());
			fileImage.transferTo(fileNew);
			listen.setImage("/haru/assets/upload/" + fileImage.getOriginalFilename());
		}
		if (listeningService.saveOrUpdate(listen) != null) {
			typeCss = "success";
			message = "Sửa thành công!!";
		} else {
			typeCss = "error";
			message = "Sửa thất bại!!";
		}
		redirectAttributes.addFlashAttribute("css", typeCss);
		redirectAttributes.addFlashAttribute("msg", message);
		return "redirect:/admin/lessons/" + id + "/listens";

	}

}
