package com.tochka.testtask.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class ViewController
{
	@RequestMapping(method = RequestMethod.GET, value = "index")
	public String getNewsPage()
	{
		return "news";
	}
}
