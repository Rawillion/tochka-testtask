package com.tochka.testtask.services;

import com.tochka.testtask.model.Subscribe;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdateNewsService
{
	private SubscribeService subscribeService;
	private NewsService newsService;

	public UpdateNewsService(SubscribeService subscribeService, NewsService newsService)
	{
		this.subscribeService = subscribeService;
		this.newsService = newsService;
	}

	@Scheduled(cron = "0 */10 * * * *")
	public void updateNews()
	{
		List<Subscribe> allSubscribes = subscribeService.getAllSubscribes();
		for (Subscribe subscribe: allSubscribes)
		{
			try
			{
				newsService.saveNews(newsService.getFreshNews(subscribe));
				System.out.println(String.format("News from %s was updated", subscribe.getSourceURL()));
			}
			catch (Exception e)
			{
				System.out.println(String.format("Error updating news from %s", subscribe.getSourceURL()));
				e.printStackTrace();
			}
		}
	}
}
