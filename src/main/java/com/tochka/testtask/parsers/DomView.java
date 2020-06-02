package com.tochka.testtask.parsers;

import com.tochka.testtask.model.ParseType;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.parser.Parser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DomView
{
	private Element root;
	private String[] newsChain;

	public DomView(InputStream content, String postPath, ParseType contentType) throws IOException
	{
		org.jsoup.nodes.Document jsoupDoc = null;
		if (contentType == ParseType.rss)
			jsoupDoc = Jsoup.parse(content, null, "", Parser.xmlParser());
		else
			jsoupDoc = Jsoup.parse(content, null, "");
		W3CDom w3cDom = new W3CDom();
		Document doc = w3cDom.fromJsoup(jsoupDoc);
		doc.getDocumentElement().normalize();
		root = doc.getDocumentElement();

		newsChain = postPath.split(" > ");
	}

	public List<Node> getNewsNodesList()
	{
		return getNodeByPath(root.getChildNodes(), 0);
	}

	private List<Node> getNodeByPath(NodeList nodeList, int chainIndex)
	{
		List<Node> matchesNodes = new NodeListProcessing(nodeList, newsChain[chainIndex++]).getMatchedNodes();

		if (matchesNodes.size() == 0 || chainIndex == newsChain.length)
		{
			return matchesNodes;
		}
		else
		{
			return getNodeByPath(matchesNodes.get(0).getChildNodes(), chainIndex);
		}
	}
}
