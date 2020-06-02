package com.tochka.testtask.services;

import com.tochka.testtask.model.News;
import com.tochka.testtask.model.Subscribe;
import com.tochka.testtask.parsers.DomView;
import com.tochka.testtask.parsers.NewsView;
import com.tochka.testtask.repositories.NewsRepository;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService
{
	private NewsRepository newsRepository;

	public NewsService(NewsRepository newsRepository)
	{
		this.newsRepository = newsRepository;
	}

	public List<News> getFreshNews(Subscribe subscribe) throws Exception
	{
		List<News> storedNews = newsRepository.getAllBySourceSubscribe(subscribe);
		List<Node> newsNodes = new DomView(new URL(subscribe.getSourceURL()).openStream(), subscribe.getNewsPath(), subscribe.getType()).getNewsNodesList();
		List<News> downloadedNews = newsNodes.stream().map(node -> new NewsView(node, subscribe)
				.getNews()).collect(Collectors.toList());
		return getUnsavedNews(downloadedNews, storedNews);
	}

	public List<News> saveNews(List<News> freshNews)
	{
		return newsRepository.saveAll(freshNews);
	}

	private List<News> getUnsavedNews(List<News> allNews, List<News> storedNews)
	{
		List<News> sortedNews = new ArrayList<>();
		for (News news : allNews)
		{
			if (storedNews.stream().noneMatch(news1 -> isContentEqual(news, news1)))
				sortedNews.add(news);
		}
		return sortedNews;
	}

	private boolean isContentEqual(News news1, News news2)
	{
		if ((news1 == null && news2 != null) || (news1 != null && news2 == null))
			return false;
		if (news1 == news2)
			return true;
		if (news1.getTitle() == null ? news2.getTitle() != null : !news1.getTitle().equals(news2.getTitle()))
			return false;
		if (news1.getDescription() == null ? news2.getDescription() != null : !news1.getDescription().equals(news2.getDescription()))
			return false;
		return true;
	}

	private List<News> getAllNews()
	{
		return newsRepository.findAll();
	}

	public List<News> getAllNews(String subTitle)
	{
		if (subTitle == null || subTitle.equals(""))
			return getAllNews();
		else
		{
			return newsRepository.getAllByTitleLike(subTitle.toLowerCase());
		}
	}
}
