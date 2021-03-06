package jlearning.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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

import jlearning.bean.CommentInfo;
import jlearning.model.Blog;
import jlearning.model.Blog.Type;
import jlearning.model.Comment;
import jlearning.service.BlogService;
import jlearning.service.CommentService;

@Controller(value = "blog")
public class BlogController extends BaseController {
	@Autowired
	private BlogService blogService;

	@Autowired
	private CommentService commentService;

	private static final Logger logger = Logger.getLogger(BlogController.class);

	@RequestMapping("/blogs")
	public String blogs(Model model) {
		checkObjectUser(model);
		logger.info("blogs index");
		List<Blog> blogs = blogService.loadBlogs();
		Collections.sort(blogs, new Comparator<Blog>() {

			public int compare(Blog o1, Blog o2) {
				// compare two instance of `Score` and return `int` as result.
				return o2.getComments().size() - o1.getComments().size();
			}
		});
		model.addAttribute("blogs", blogs);
		List<Blog> newBlogs = blogService.loadNewBlogs();
		newBlogs = newBlogs.subList(0, 3);
		model.addAttribute("news", newBlogs);
		List<Blog> Blogs1 = blogService.loadBlogsByType(Type.culture);
		if (Blogs1.size() > 6)
			Blogs1 = Blogs1.subList(0, 5);
		model.addAttribute("blogs1", Blogs1);
		List<Blog> Blogs2 = blogService.loadBlogsByType(Type.experience);
		if (Blogs2.size() > 6)
			Blogs2 = Blogs2.subList(0, 5);
		model.addAttribute("blogs2", Blogs2);
		List<Blog> Blogs3 = blogService.loadBlogsByType(Type.general);
		if (Blogs3.size() > 6)
			Blogs3 = Blogs3.subList(0, 5);
		model.addAttribute("blogs3", Blogs3);
		return "views/web/blog/index";
	}

	@RequestMapping("/blogs/{id}")
	public String blog(@PathVariable("id") int id, Model model) {
		checkObjectUser(model);
		logger.info("show blog");
		Blog blog = blogService.findById(id);
		Blog nextBlog = blogService.findById(id + 1);
		Blog preBlog = blogService.findById(id - 1);
		List<Blog> newBlogs = blogService.loadNewBlogs();
		newBlogs = newBlogs.subList(0, 3);
		model.addAttribute("news", newBlogs);
		List<Blog> sameTypeBlogs = blogService.loadBlogsByType(blog.getType());
		for (Blog a : sameTypeBlogs) {
			if (a.getId() == blog.getId()) {
				sameTypeBlogs.remove(a);
				break;
			}
		}
		if (sameTypeBlogs.size() >= 3)
			sameTypeBlogs = getRandomElement(sameTypeBlogs, 3);
		if (nextBlog != null)
			model.addAttribute("nextBlog", nextBlog);
		if (preBlog != null)
			model.addAttribute("preBlog", preBlog);
		if (sameTypeBlogs != null) {
			model.addAttribute("sameType", sameTypeBlogs);
		}
		model.addAttribute("blog", blog);
		CommentInfo cmt = new CommentInfo();
		model.addAttribute("newComment", cmt);
		return "views/web/blog/post";
	}

	@RequestMapping("/blogs/category/{id}")
	public String showByCategory(@PathVariable("id") int id,
			@RequestParam(name = "page", defaultValue = "1") Integer page, Model model) {
		checkObjectUser(model);
		logger.info("show by category");
		Type t = null;
		if (id == 1)
			t = Type.culture;
		if (id == 2)
			t = Type.experience;
		if (id == 3)
			t = Type.general;
		// List<Blog> sameTypeBlogs = blogService.loadBlogsByType(t);
		List<Blog> sameTypeBlogs = blogService.searchByPaging(id, "", page);
		int sumProductOfCategory = (int) sameTypeBlogs.size();
		model.addAttribute("sumProductOfCategory", sumProductOfCategory);
		model.addAttribute("blogs", sameTypeBlogs);
		List<Blog> newBlogs = blogService.loadNewBlogs();
		newBlogs = newBlogs.subList(0, 3);
		model.addAttribute("news", newBlogs);
		return "views/web/blog/search";
	}

	@RequestMapping("/blogs/search")
	public String search(@RequestParam(name = "name1", required = false) String blogName, Model model) {
		checkObjectUser(model);
		logger.info("Search blog");
		List<Blog> blogs = blogService.search(blogName);
		model.addAttribute("blogs", blogs);
		List<Blog> newBlogs = blogService.loadNewBlogs();
		newBlogs = newBlogs.subList(0, 3);
		model.addAttribute("news", newBlogs);
		return "views/web/blog/search";
	}

	@RequestMapping("/blogs/{id}/comment")
	public String comment(@ModelAttribute("newComment") CommentInfo comment, @PathVariable("id") int id, Model model,
			HttpServletRequest request, final RedirectAttributes redirectAttributes) {
		checkObjectUser(model);
		logger.info("new comment");
		HttpSession session = request.getSession();
		comment.setBlogId(id);
		comment.setUserId(Integer.parseInt(session.getAttribute("currentUser").toString()));

		if (commentService.createComment(comment) != null) {
			redirectAttributes.addFlashAttribute("addComment", "Bạn đã thêm 1 bình luận");

		} else {
			redirectAttributes.addFlashAttribute("addComment", "Thêm bình luận không thành côngs");
		}

		return "redirect:/blogs/" + id;
	}

	@RequestMapping("/blogs/{id}/comments/{cmtId}/delete")
	public String deleteComment(final RedirectAttributes redirectAttributes, @PathVariable("id") int blogId,
			@PathVariable("cmtId") int cmtId, Model model, HttpServletRequest request) {
		checkObjectUser(model);
		Comment cmt = commentService.findById(cmtId);
		if (commentService.delete(cmt) == true) {
			redirectAttributes.addFlashAttribute("deleteComment", "Bạn đã xóa 1 bình luận");
		} else {
			redirectAttributes.addFlashAttribute("deleteComment", "Xóa bình luận thất bại");

		}
		return "redirect:/blogs/" + blogId;

	}

	@RequestMapping("/blogs/comments/{cmtId}/{string}/save")
	public String updateComment(final RedirectAttributes redirectAttributes, @PathVariable("cmtId") int cmtId,
			@PathVariable("string") String content,
			Model model, HttpServletRequest request) {
		checkObjectUser(model);
		Comment cmt = commentService.findById(cmtId);
		cmt.setContent(content);
		if (commentService.saveOrUpdate(cmt) != null) {
			redirectAttributes.addFlashAttribute("deleteComment", "Bạn đã sửa bình luận");
		} else {
			redirectAttributes.addFlashAttribute("deleteComment", "Sửa bình luận thất bại");

		}
		return "redirect:/blogs/" + cmt.getBlog().getId();

	}

	private List<Blog> getRandomElement(List<Blog> list, int totalItems) {
		Random rand = new Random();

		List<Blog> newList = new ArrayList<Blog>();

		for (int i = 0; i < totalItems; i++) {

			int randomIndex = rand.nextInt(list.size());
			if (newList.contains(list.get(randomIndex)) == false)
				newList.add(list.get(randomIndex));
		}
		return newList;
	}

}
