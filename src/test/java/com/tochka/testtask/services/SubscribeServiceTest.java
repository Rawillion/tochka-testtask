package com.tochka.testtask.services;

import com.tochka.testtask.model.Subscribe;
import com.tochka.testtask.repositories.SubscribeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class SubscribeServiceTest
{
	@Mock
	SubscribeRepository subscribeRepository;

	@InjectMocks
	SubscribeService subscribeService;

	List<Subscribe> subscribes = new ArrayList<>();

	@BeforeEach
	void setup()
	{
		MockitoAnnotations.initMocks(this);

		for (int i = 0; i < 4; ++i)
		{
			Subscribe subscribe = new Subscribe();
			subscribe.setSourceURL("site" + (i + 1));
			subscribe.setTitlePath("title" + (i + 1));
			subscribes.add(subscribe);
		}

		when(subscribeRepository.findAll()).thenReturn(subscribes);
		when(subscribeRepository.getAllBySourceURL("site2")).thenReturn(subscribes.stream().filter(subscribe -> subscribe.getSourceURL().equals("site2")).collect(Collectors.toList()));
	}

	@Test
	void getAllSubscribes()
	{
		List<Subscribe> subscribeList = subscribeService.getAllSubscribes(null);
		assert(subscribeList == subscribes);
		subscribeList = subscribeService.getAllSubscribes("site2");
		assert(subscribeList.size() == 1);
		assert(subscribeList.get(0).getSourceURL().equals("site2"));
	}

	@Test
	void addOrUpdateSubscribe()
	{
		Subscribe newSubscribe = new Subscribe();
		newSubscribe.setSourceURL("site10");
		subscribeService.addOrUpdateSubscribe(newSubscribe);
		Mockito.verify(subscribeRepository).save(newSubscribe);

		newSubscribe.setSourceURL("site2");
		newSubscribe.setDescriptionPath("description2");
		newSubscribe = subscribeService.addOrUpdateSubscribe(newSubscribe);
		assert(newSubscribe.getDescriptionPath().equals("description2"));
		assert(newSubscribe.getTitlePath() == null);
		Mockito.verify(subscribeRepository, times(2)).save(newSubscribe);// second time
	}
}