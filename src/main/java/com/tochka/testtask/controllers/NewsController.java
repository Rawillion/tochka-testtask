package com.tochka.testtask.controllers;

import com.tochka.testtask.model.News;
import com.tochka.testtask.model.Subscribe;
import com.tochka.testtask.services.NewsService;
import com.tochka.testtask.services.SubscribeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController
{
	private NewsService newsService;

	public NewsController(NewsService newsService)
	{
		this.newsService = newsService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<News> getAllNews(@RequestParam(value = "title", required = false, defaultValue = "") String title)
	{
		return newsService.getAllNews(title);
	}

}
