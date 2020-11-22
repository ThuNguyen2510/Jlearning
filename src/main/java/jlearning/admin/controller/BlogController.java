package jlearning.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jlearning.model.Blog;

import jlearning.service.BlogService;

@Controller
@RequestMapping("/admin/blogs")
public class BlogController {
	@Autowired
	private BlogService blogService;

	@GetMapping(value = { "", "/" })
	public String index(Model model) {
		List<Blog> blogs = blogService.loadBlogs();
		model.addAttribute("blogs", blogs);
		return "views/admin/blog/blogs";
	}
	
	@GetMapping(value = { "/{id}"})
	public String show(@PathVariable("id") int id,Model model) {
		Blog blog = blogService.findById(id);
		model.addAttribute("blog", blog);
		return "views/admin/blog/blog";
	}
	
	@GetMapping(value = { "/{id}/delete"})
	public String delelte(@PathVariable("id") int id,Model model) {
		blogService.delete(blogService.findById(id));
		
		return "redirect:/admin/blogs";
	}
}
