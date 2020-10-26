package jlearning.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jlearning.model.Blog;
import jlearning.service.BlogService;

@Controller(value = "blog")
public class BlogController extends BaseController {
	@Autowired
	private BlogService blogService;
	private static final Logger logger = Logger.getLogger(BlogController.class);

	@RequestMapping("/blogs")
	public String blogs(Model model) {
		checkObjectUser(model);
		logger.info("blogs index");
		List<Blog> blogs = blogService.loadBlogs();
		Collections.sort(blogs, new Comparator<Blog>() {

	        public int compare(Blog o1, Blog o2) {
	            // compare two instance of `Score` and return `int` as result.
	            return o2.getComments().size()-o1.getComments().size();
	        }
	    });
		model.addAttribute("blogs", blogs);
		List<Blog> newBlogs = blogService.loadNewBlogs();
		newBlogs=newBlogs.subList(0, 3);
		model.addAttribute("news",newBlogs);
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
		newBlogs=newBlogs.subList(0, 3);
		model.addAttribute("news",newBlogs);
		List<Blog> sameTypeBlogs = blogService.loadBlogsByType(blog);
		
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

		return "views/web/blog/post";
	}

	private List<Blog> getRandomElement(List<Blog> list, int totalItems) {
		Random rand = new Random();

		List<Blog> newList = new ArrayList<Blog>();
		
		for (int i = 0; i < totalItems; i++) {

			int randomIndex = rand.nextInt(list.size());
			if(newList.contains(list.get(randomIndex))==false)
			newList.add(list.get(randomIndex));
		}
		return newList;
	}

}
