package com.tochka.testtask.services;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.tochka.testtask.model.News;
import com.tochka.testtask.model.ParseType;
import com.tochka.testtask.model.Subscribe;
import com.tochka.testtask.repositories.NewsRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest
{
	@Mock
	NewsRepository newsRepository;

	@InjectMocks
	NewsService newsService;

	HttpServer httpServer;

	String testXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
					 "                    <rss version=\"2.0\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
					 "                      <channel>\n" +
					 "                        <language>ru</language>\n" +
					 "                        <title>Lenta.ru : Новости</title>\n" +
					 "                        <description>Новости, статьи, фотографии, видео. Семь дней в неделю, 24 часа в сутки.</description>\n" +
					 "                        <item>\n" +
					 "                          <title>title1</title>\n" +
					 "                          <description>description1</description>\n" +
					 "                          <pubDate>Wed, 27 May 2020 16:58:13 +0300</pubDate>\n" +
					 "                        </item>\n" +
					 "                        <item>\n" +
					 "                          <title>title2</title>\n" +
					 "                          <description>description2</description>\n" +
					 "                          <pubDate>Wed, 27 May 2020 16:57:32 +0300</pubDate>\n" +
					 "                        </item>\n" +
					 "                        <item>\n" +
					 "                          <title>title3</title>\n" +
					 "                          <description>description3</description>\n" +
					 "                          <pubDate>Wed, 20 May 2020 16:57:32 +0300</pubDate>\n" +
					 "                        </item>\n" +
					 "                      </channel>\n" +
					 "                    </rss>";

	List<News> newsList;
	Subscribe subscribe;

	@BeforeEach
	void setup() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		httpServer = HttpServer.create(new InetSocketAddress(8023), 0); // or use InetSocketAddress(0) for ephemeral port
		httpServer.createContext("/testurl", new HttpHandler() {
			public void handle(HttpExchange exchange) throws IOException
			{
				byte[] response = testXML.getBytes();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
				exchange.getResponseBody().write(response);
				exchange.close();
			}
		});
		httpServer.start();

		subscribe = new Subscribe();
		subscribe.setSourceURL("http://localhost:8023/testurl");
		subscribe.setNewsPath("channel > item");
		subscribe.setTitlePath("title");
		subscribe.setDescriptionPath("description");
		subscribe.setType(ParseType.rss);

		newsList = new ArrayList<>();
		for (long i = 0; i < 3 ; ++i)
		{
			News news = new News();
			news.setTitle("title" + i);
			news.setDescription("description" + i);
			news.setSourceSubscribe(subscribe);
			newsList.add(news);
		}

		subscribe.setNews(newsList);
	}

	@Test
	void getFreshNews() throws Exception
	{
		when(newsRepository.getAllBySourceSubscribe(subscribe)).thenReturn(newsList);

		List<News> freshNews = newsService.getFreshNews(subscribe);

		assert(freshNews.size() == 1);
		assert(freshNews.get(0).getTitle().equals("title3"));
		assert(freshNews.get(0).getDescription().equals("description3"));
	}

	@Test
	void saveNews()
	{
		newsService.saveNews(newsList);
		Mockito.verify(newsRepository).saveAll(newsList);
	}

	@Test
	void getAllNews()
	{
		String testTitle = "1";
		when(newsRepository.findAll()).thenReturn(newsList);
		when(newsRepository.getAllByTitleLike(testTitle)).thenReturn(newsList.stream().filter(news -> news.getTitle().contains(testTitle))
																			 .collect(Collectors.toList()));

		List<News> testNews = newsService.getAllNews("");
		assert(testNews == newsList);
		testNews = newsService.getAllNews(testTitle);
		assert(testNews.size() == 1);
		assert(testNews.get(0).getTitle().contains(testTitle));
	}

	@AfterEach
	void teardown()
	{
		httpServer.stop(0);
	}
}