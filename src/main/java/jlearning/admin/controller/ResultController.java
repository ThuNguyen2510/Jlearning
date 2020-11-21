package jlearning.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jlearning.model.Result;
import jlearning.service.ResultService;

@Controller
@RequestMapping("/admin/results")
public class ResultController {
	@Autowired
	private ResultService resultService;
	@GetMapping(value = { "", "/" })
	public String index(Model model) {
		List<Result> results = resultService.loadAllResult();
		model.addAttribute("results", results);
		return "views/admin/result/results";
	}
}
