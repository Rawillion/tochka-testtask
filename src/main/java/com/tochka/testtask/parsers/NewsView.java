package com.tochka.testtask.parsers;

import com.tochka.testtask.model.News;
import com.tochka.testtask.model.Subscribe;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NewsView
{
	private Node newsNode;
	private String[] titleChain;
	private String[] descriptionChain;
	private Subscribe subscribe;

	public NewsView(Node newsNode, Subscribe subscribe)
	{
		this.newsNode = newsNode;
		this.subscribe = subscribe;
		this.titleChain = subscribe.getTitlePath().split(" > ");
		this.descriptionChain = subscribe.getDescriptionPath().split(" > ");
	}

	public News getNews()
	{
		String title = getNewsContentByPath(newsNode, titleChain);
		String description = getNewsContentByPath(newsNode, descriptionChain);
		News news = new News();
		news.setTitle(title);
		news.setDescription(description);
		news.setSourceSubscribe(subscribe);
		return news;
	}

	private String getNewsContentByPath(Node currentNode, String[] path)
	{
		List<Node> matchesNodes = getNodeByPath(currentNode.getChildNodes(), path, 0);
		if (matchesNodes.size() == 0)
			return "";
		StringBuilder content = new StringBuilder();
		matchesNodes.forEach(node -> content.append(getNodeValue(node)));
		return content.toString();
	}

	private List<Node> getNodeByPath(NodeList nodeList, String[] chain, int chainIndex)
	{
		List<Node> matchesNodes = new NodeListProcessing(nodeList, chain[chainIndex++]).getMatchedNodes();

		if (matchesNodes.size() == 0 || chainIndex == chain.length)
		{
			return matchesNodes;
		}
		else
		{
			return getNodeByPath(matchesNodes.get(0).getChildNodes(), chain, chainIndex);
		}
	}

	private String getNodeValue(Node node)
	{
		StringBuilder result = new StringBuilder();
		result.append(Optional.ofNullable(node.getNodeValue()).orElse(""));

		NodeList nodeList = node.getChildNodes();
		if (nodeList.getLength() == 0)
			return result.toString();
		else
		{
			for (int i = 0; i < nodeList.getLength(); ++i)
			{
				Node nestedNode = nodeList.item(i);
				result.append(Optional.ofNullable(nestedNode.getNodeValue()).orElse(""));
				for (int j = 0; j < nestedNode.getChildNodes().getLength(); ++j)
				{
					result.append(getNodeValue(nestedNode.getChildNodes().item(j)));
				}
			}
			return result.toString();
		}
	}
}
