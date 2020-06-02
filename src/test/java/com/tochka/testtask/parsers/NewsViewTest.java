package com.tochka.testtask.parsers;

import com.tochka.testtask.model.News;
import com.tochka.testtask.model.ParseType;
import com.tochka.testtask.model.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

class NewsViewTest
{
	String simpleXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
					   "                    <rss version=\"2.0\" class=\"rssclass main secondary\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
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

	InputStream simpleHTML = null;

	@BeforeEach
	void setup() throws IOException
	{
		simpleHTML = new FileInputStream(new File("src/test/resources/Habr.html"));
	}

	@Test
	void getNewsFromRSS() throws Exception
	{
		DomView domView = new DomView(new ByteArrayInputStream(simpleXML.getBytes()), "channel > item", ParseType.rss);
		List<Node> nodes = domView.getNewsNodesList();

		Subscribe subscribe = new Subscribe();
		subscribe.setTitlePath("title");
		subscribe.setDescriptionPath("description");

		List<News> news = nodes.stream().map(node -> new NewsView(node, subscribe).getNews()).collect(Collectors.toList());
		for (int i = 0; i < news.size(); ++i)
		{
			News currentNews = news.get(i);
			assert(currentNews.getTitle().equals("title" + (i + 1)));
			assert(currentNews.getDescription().equals("description" + (i + 1)));
			assert(currentNews.getSourceSubscribe() == subscribe);
		}
	}

	@Test
	void getNewsFromHTML() throws Exception
	{
		String parseRule = "body > div:class(layout) > div:class(%layout__row_body) > div > section > div:class(content_left%) > div:class(posts_list) > ul > li:id(post%)";
		DomView domView = new DomView(simpleHTML, parseRule, ParseType.html);
		List<Node> nodes = domView.getNewsNodesList();

		assert(nodes.size() == 20);

		Subscribe subscribe = new Subscribe();
		subscribe.setTitlePath("article > h2 > a");
		subscribe.setDescriptionPath("article > div > div");

		List<News> news = nodes.stream().map(node -> new NewsView(node, subscribe).getNews()).collect(Collectors.toList());
		assert(news.get(0).getTitle().equals("Самые эффективные сервисы онлайн-уроков для учеников и преподавателей: пятерка лучших"));
		assert(news.get(0).getDescription().contains("Дистанционное обучение сейчас по всем нам понятным причинам становится все популярнее."));
	}
}