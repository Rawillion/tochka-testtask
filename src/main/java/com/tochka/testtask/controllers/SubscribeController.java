package com.tochka.testtask.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tochka.testtask.model.ParseType;
import com.tochka.testtask.model.Subscribe;
import com.tochka.testtask.services.NewsService;
import com.tochka.testtask.services.SubscribeService;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subscribe")
public class SubscribeController
{
	private SubscribeService subscribeService;

	public SubscribeController(SubscribeService subscribeService)
	{
		this.subscribeService = subscribeService;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String proceedSubscribe(@RequestHeader("source") String source, @RequestHeader(value = "type") ParseType type,
								   @RequestBody Map<String, String> parseRule) throws JsonProcessingException
	{
		try
		{
			Subscribe newSubscribe = new Subscribe();
			newSubscribe.setSourceURL(source);
			newSubscribe.setTitlePath(parseRule.get("title"));
			newSubscribe.setDescriptionPath(parseRule.get("description"));
			newSubscribe.setNewsPath(parseRule.get("news"));
			newSubscribe.setType(type);

			newSubscribe = subscribeService.addOrUpdateSubscribe(newSubscribe);
			return new ObjectMapper().writeValueAsString(newSubscribe);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", e.getMessage());
			return new ObjectMapper().writeValueAsString(errorResponse);
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<Subscribe> getSubscribe(@RequestParam(value = "site", required = false, defaultValue = "") String site)
	{
		return subscribeService.getAllSubscribes(site);
	}
}
