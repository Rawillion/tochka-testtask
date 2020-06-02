package com.tochka.testtask.parsers;

import com.tochka.testtask.model.ParseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import java.io.*;
import java.util.List;

class DomViewTest
{
	String simpleXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
					   "                    <rss version=\"2.0\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n" +
					   "                      <channel>\n" +
					   "                        <language>ru</language>\n" +
					   "                        <title>Lenta.ru : Новости</title>\n" +
					   "                        <description>Новости, статьи, фотографии, видео. Семь дней в неделю, 24 часа в сутки.</description>\n" +
					   "                        <item>\n" +
					   "                          <title>title1</title>\n" +
					   "                          <description>\n" +
					   "                            description1\n" +
					   "                          </description>\n" +
					   "                          <pubDate>Wed, 27 May 2020 16:58:13 +0300</pubDate>\n" +
					   "                        </item>\n" +
					   "                        <item>\n" +
					   "                          <title>title2</title>\n" +
					   "                          <description>\n" +
					   "                            description2\n" +
					   "                          </description>\n" +
					   "                          <pubDate>Wed, 27 May 2020 16:57:32 +0300</pubDate>\n" +
					   "                        </item>\n" +
					   "                        <item>\n" +
					   "                          <title>title3</title>\n" +
					   "                          <description>\n" +
					   "                            description3\n" +
					   "                          </description>\n" +
					   "                          <pubDate>Wed, 20 May 2020 16:57:32 +0300</pubDate>\n" +
					   "                        </item>\n" +
					   "                      </channel>\n" +
					   "<nesteditem>" +
					   "</nesteditem>" +
					   "                    </rss>";

	InputStream simpleHTML = null;

	@BeforeEach
	void setup() throws IOException
	{
		simpleHTML = new FileInputStream(new File("src/test/resources/Habr.html"));
	}

	@Test
	void getNewsNodes() throws Exception
	{
		//rss
		DomView domView = new DomView(new ByteArrayInputStream(simpleXML.getBytes()), "channel > item", ParseType.rss);
		List<Node> nodes = domView.getNewsNodesList();
		assert(nodes.size() == 3);
		for (Node itemNode : nodes)
		{
			assert(itemNode.getNodeName().equals("item"));
		}

		//html
		String parseRule = "body > div:class(layout) > div:class(%layout__row_body) > div > section > div:class(content_left%) > div:class(posts_list) > ul > li:id(post%)";
		domView = new DomView(simpleHTML, parseRule, ParseType.html);
		nodes = domView.getNewsNodesList();
		assert(nodes.size() == 20);
		for (Node itemNode : nodes)
		{
			assert(itemNode.getNodeName().equals("li"));
			assert(itemNode.getAttributes().getNamedItem("id").getNodeValue().toLowerCase().startsWith("post"));
		}
	}
}