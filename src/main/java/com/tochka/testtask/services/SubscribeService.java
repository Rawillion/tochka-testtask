package com.tochka.testtask.services;

import com.tochka.testtask.model.Subscribe;
import com.tochka.testtask.repositories.SubscribeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscribeService
{
	private SubscribeRepository subscribeRepository;

	public SubscribeService(SubscribeRepository subscribeRepository)
	{
		this.subscribeRepository = subscribeRepository;
	}

	public List<Subscribe> getAllSubscribes()
	{
		return subscribeRepository.findAll();
	}

	public List<Subscribe> getAllSubscribes(String siteUrl)
	{
		if (siteUrl == null || siteUrl.equals(""))
			return getAllSubscribes();
		else
			return subscribeRepository.getAllBySourceURL(siteUrl);
	}

	public Subscribe addOrUpdateSubscribe(Subscribe subscribe)
	{
		List<Subscribe> existSubscribe = subscribeRepository.getAllBySourceURL(subscribe.getSourceURL());
		if (existSubscribe.size() > 0)
		{
			Subscribe storedSubscribe = existSubscribe.get(0);
			storedSubscribe.setNewsPath(subscribe.getNewsPath());
			storedSubscribe.setDescriptionPath(subscribe.getDescriptionPath());
			storedSubscribe.setTitlePath(subscribe.getTitlePath());
			subscribeRepository.save(storedSubscribe);
			return storedSubscribe;
		}
		else
		{
			subscribeRepository.save(subscribe);
			return subscribe;
		}
	}
}
