package jlearning.web.controller;

import java.io.File;
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

import jlearning.bean.LessonHis;
import jlearning.bean.UserInfo;
import jlearning.model.Blog;
import jlearning.model.History;
import jlearning.model.User;
import jlearning.model.Test.Type;
import jlearning.model.Lesson;
import jlearning.model.Result;
import jlearning.model.User.Role;
import jlearning.service.BlogService;
import jlearning.service.LessonService;
import jlearning.service.UserService;
import jlearning.validation.UserValidation;

@Controller(value = "user")
public class UserController extends BaseController {

	private static final Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private LessonService lessonService;

	@Autowired
	private BlogService blogService;

	@GetMapping("/users/{id}")
	public String index(@PathVariable("id") int userId, Model model, HttpServletRequest request) {
		checkObjectUser(model);
		User u = userService.findById(userId);
		UserInfo userInfo = new UserInfo();
		userInfo.setId(u.getId());
		userInfo.setEmail(u.getEmail());
		userInfo.setName(u.getName());
		userInfo.setPassword(u.getPassword());
		userInfo.setLevel(u.getLevel());
		userInfo.setRole(u.getRole());
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("userFix", u);
		return "views/web/user/index";
	}

	@RequestMapping("/users/{id}/edit")
	public String edit(@PathVariable("id") int userId, @ModelAttribute("userInfo") UserInfo user, BindingResult result,
			Model model, final RedirectAttributes redirectAttributes, HttpServletRequest request) {
		checkObjectUser(model);
		UserValidation userVali = new UserValidation();
		userVali.validate(user, result);
		HttpSession session = request.getSession();
		deleteSession(session);
		if (result.hasErrors() == false) {

			User us = user.convertToUser();
			us.setId(userId);
			us.setRole(Role.USER);
			deleteSession(session);
			if (userService.saveOrUpdate(us) == null) {

				redirectAttributes.addFlashAttribute("css", "Thất bại");
			} else {

				redirectAttributes.addFlashAttribute("css", "Thay đổi thông tin thành công");
			}
		} else {
			session.setAttribute("error", "error");

			if (result.getFieldValue("password") != null) {
				session.setAttribute("error1", "errorPass");

			}
			if (result.getFieldValue("confirmPassword") != null) {
				session.setAttribute("error2", "errorConfirm");

			}

		}

		return "redirect:/users/" + userId;
	}

	@GetMapping("/users/{id}/examHis")
	public String exams(Model model, @PathVariable("id") int userId, HttpServletRequest request) {
		checkObjectUser(model);
		HttpSession session = request.getSession();
		deleteSession(session);
		User u = userService.findById(userId);
		model.addAttribute("userFix", u);
		List<Result> rsLevels = new ArrayList<Result>();
		List<Result> rsLessons = new ArrayList<Result>();
		List<Result> rsExams = new ArrayList<Result>();
		for (int i = 0; i < u.getResults().size(); i++) {
			Result rs = u.getResults().get(i);
			if (rs.getTest().getType() == Type.LEVEL) {
				rsLevels.add(rs);
			}
			if (rs.getTest().getType() == Type.LESSON) {
				rsLessons.add(rs);
			}
			if (rs.getTest().getType() == Type.EXAM) {
				rsExams.add(rs);
			}
		}
		model.addAttribute("rsLevels", rsLevels);
		model.addAttribute("rsLessons", rsLessons);
		model.addAttribute("rsExams", rsExams);
		return "views/web/user/examHistory";
	}

	@GetMapping("/users/{id}/lessonHis")
	public String lessons(@PathVariable("id") int userId, Model model, HttpServletRequest request) {
		checkObjectUser(model);
		HttpSession session = request.getSession();
		deleteSession(session);
		User u = userService.findById(userId);
		model.addAttribute("userFix", u);
		List<History> lessonHis = userService.loadHistory(userId, 2);// lesson:2
		List<LessonHis> list = new ArrayList<LessonHis>();
		if (lessonHis != null) {
			for (int i = 0; i < lessonHis.size(); i++) {
				LessonHis lesson = new LessonHis();
				if (lessonService.findById(lessonHis.get(i).getObjectId()) != null) {
					Lesson lesson_ = lessonService.findById(lessonHis.get(i).getObjectId());
					lesson.setObjectId(lesson_.getId());
					lesson.setLessonName(lesson_.getName());
					lesson.setCourseName(lesson_.getCourse().getName());
					lesson.setCreate_time(lessonHis.get(i).getCreate_time());
					lesson.setCourseId(lesson_.getCourse().getId());
					list.add(lesson);
				}

			}
		}

		model.addAttribute("lessonHis", list);
		return "views/web/user/lessonHistory";
	}

	@GetMapping("/users/{id}/blogs")
	public String blogs(@PathVariable("id") int userId, Model model, HttpServletRequest request) {
		checkObjectUser(model);
		HttpSession session = request.getSession();
		deleteSession(session);
		User u = userService.findById(userId);
		model.addAttribute("userFix", u);
		List<Blog> blogs = userService.loadBlogs(userId);
		model.addAttribute("blogs", blogs);
		return "views/web/user/myBlogs";
	}

	@GetMapping("/users/{id}/blogs/new")
	public String newBlog(@PathVariable("id") int userId, Model model, HttpServletRequest request) {
		checkObjectUser(model);
		HttpSession session = request.getSession();
		deleteSession(session);
		User u = userService.findById(userId);
		model.addAttribute("userFix", u);
		model.addAttribute("blogForm", new Blog());
		List<jlearning.model.Blog.Type> types = new ArrayList<>();
		types.add(jlearning.model.Blog.Type.culture);
		types.add(jlearning.model.Blog.Type.experience);
		types.add(jlearning.model.Blog.Type.general);
		model.addAttribute("types", types);
		return "views/web/user/newBlog";
	}

	@GetMapping("/users/{id}/blogs/{blogId}")
	public String blog(@PathVariable("id") int userId, @PathVariable("blogId") int blogId, Model model,
			HttpServletRequest request) {
		checkObjectUser(model);
		HttpSession session = request.getSession();
		deleteSession(session);
		User u = userService.findById(userId);
		model.addAttribute("userFix", u);
		Blog blog = blogService.findById(blogId);
		model.addAttribute("blog", blog);
		model.addAttribute("status", "view");
		return "views/web/user/viewBlog";
	}

	@GetMapping("/users/{id}/blogs/{blogId}/edit")
	public String blogEdit(@PathVariable("id") int userId, @PathVariable("blogId") int blogId, Model model,
			HttpServletRequest request) {
		checkObjectUser(model);
		HttpSession session = request.getSession();
		deleteSession(session);
		User u = userService.findById(userId);
		model.addAttribute("userFix", u);
		Blog blog = blogService.findById(blogId);
		model.addAttribute("blog", blog);
		List<jlearning.model.Blog.Type> types = new ArrayList<>();
		types.add(jlearning.model.Blog.Type.culture);
		types.add(jlearning.model.Blog.Type.experience);
		types.add(jlearning.model.Blog.Type.general);
		model.addAttribute("types", types);
		model.addAttribute("status", "update");
		return "views/web/user/viewBlog";
	}

	@RequestMapping("/users/{id}/blogs/{blogId}/save")
	public String blogSave(@PathVariable("id") int userId, @PathVariable("blogId") int blogId, Model model,
			@ModelAttribute("blog") Blog blog, @RequestParam(value = "image", required = false) MultipartFile file)
			throws IllegalStateException, IOException {

		checkObjectUser(model);
		/*
		 * File fileNew = new File(
		 * "C:/Users/nguye/eclipse-workspace/Jlearning/src/main/webapp/assets/upload",
		 * file.getOriginalFilename()); file.transferTo(fileNew); if(file!=null) {
		 * blog.setImage("/haru/assets/upload/" + file.getOriginalFilename()); }
		 */
		blogService.saveOrUpdate(blog);
		return "redirect:/users/" + userId + "/blogs";
	}

	@RequestMapping("/users/{id}/blogs/save")
	public String blogCreate(@PathVariable("id") int userId, Model model, @ModelAttribute("blogForm") Blog blog,
			@RequestParam("imageFile") MultipartFile file)
			throws IllegalStateException, IOException {
		checkObjectUser(model);
		File fileNew = new File("C:/Users/nguye/eclipse-workspace/Jlearning/src/main/webapp/assets/upload",
				file.getOriginalFilename());
		file.transferTo(fileNew);
		blog.setImage("/haru/assets/upload/" + file.getOriginalFilename());
		blogService.createBlog(blog, userId);
		return "redirect:/users/" + userId + "/blogs";
	}

	@RequestMapping("/users/{id1}/blogs/{id2}/delete")
	public String blogDelete(@PathVariable("id2") int blogId,@PathVariable("id1") int userId) {
		Blog blog = blogService.findById(blogId);
		blogService.delete(blog);
		return "redirect:/users/" + userId + "/blogs";
	}
	private void deleteSession(HttpSession session) {
		if (session.getAttribute("error") != null) {
			session.removeAttribute("error");
			if (session.getAttribute("error1") != null) {
				session.removeAttribute("error1");

			}
			if (session.getAttribute("error2") != null) {
				session.removeAttribute("error2");

			}
		}
	}
}
