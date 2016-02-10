package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class AuctionSearch implements IAuctionSearch {
	
	private IndexSearcher searcher = null;
    private QueryParser parser = null;
	
	private int totalHits = 0;
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		
		try
		{
			searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index"))));;
			parser = new QueryParser("Content", new StandardAnalyzer());
			
			int numResultsTotal = numResultsToSkip + numResultsToReturn;
			
			Query q = parser.parse(query);
			//get the top search hits
	        TopDocs hits = searcher.search(q, numResultsTotal);
	        ScoreDoc[] top_results = hits.scoreDocs;
	        int num_TotalHits = hits.totalHits;
			totalHits = hits.totalHits;
	        SearchResult[] result_list = new SearchResult[top_results.length - numResultsToSkip];
	        
	        for (int i = numResultsToSkip; i < top_results.length; i++)
	        {
	        	Document doc = searcher.doc(top_results[i].doc);
	        	SearchResult temp = new SearchResult(doc.get("ItemID"), doc.get("Name"));
	        	result_list[i-numResultsToSkip] = temp;
	        }
	        
	        return result_list;
		}
	
		catch (Exception e)
		{
			System.out.println(e);
		}
		
		return new SearchResult[0];
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		Connection conn = null;
    	PreparedStatement stmt = null;
    	ResultSet items_results;
    	Double Latitude; 
		Double Longitude;
    	int tmpLength;
		String tmp;
    	String[] tmp2;
		
		try {
			conn = DbManager.getConnection(true);
			
		} catch (SQLException ex) {
			System.out.println(ex);
		}
		
		SearchResult[] basic_search_results = basicSearch(query,numResultsToSkip,numResultsToReturn);
    	basic_search_results = basicSearch(query,numResultsToSkip,totalHits);
    	ArrayList<SearchResult> final_results = new ArrayList<SearchResult>();
    	for(SearchResult res : basic_search_results) {
    		try {
				stmt = conn.prepareStatement("SELECT ItemID,astext(Location) FROM ItemLocation WHERE ItemID = " + res.getItemId());
				items_results = stmt.executeQuery();
				if(items_results.next()) {
					tmp = items_results.getString("astext(Location)");
					tmpLength = tmp.length();
					tmp = tmp.substring(6,tmpLength-1);
					tmp2 = tmp.split(" ");
					Latitude = Double.parseDouble(tmp2[0]);
					Longitude = Double.parseDouble(tmp2[1]);
					if(Latitude <= region.getRx() && Longitude <= region.getRy()
							&& Latitude >= region.getLx() && Longitude >= region.getLy())
						final_results.add(res);
					}
    			} catch (SQLException e) {
					e.printStackTrace();
    			}
    		}
    		try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        return final_results.toArray(new SearchResult[final_results.size()]);
	}

	public String getXMLDataForItemId(String itemId) {
		Connection conn = null;
		
		try {
			conn = DbManager.getConnection(true);
			Statement stmt = conn.createStatement();
			
			ResultSet results = stmt.executeQuery("SELECT * FROM Item WHERE ItemID = " + itemId);
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			org.w3c.dom.Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Item");
			doc.appendChild(rootElement);
			rootElement.setAttribute("ItemID", itemId);
			
			if (results.first())
			{
				//append name
				String itemName = results.getString("Name");
				Element name = doc.createElement("Name");
				name.appendChild(doc.createTextNode(itemName));
				rootElement.appendChild(name);
				
				String currently = results.getString("Currently");
				String buyPrice = results.getString("Buy_Price");
				String firstBid = results.getString("First_Bid");
				String numberOfBids = results.getString("Number_of_Bids");
				String country = results.getString("Country");
				String started = results.getString("Started");
				started = formatDate(started);
				String ends = results.getString("Ends");
				ends = formatDate(ends);
				String description = results.getString("Description");
				
				//get latitude and longitude
				String latitude = results.getString("Latitude");
				String longitude = results.getString("Longitude");
				
				//get seller's rating and userID
				String userId = results.getString("UserID");
				results = stmt.executeQuery("SELECT * FROM User WHERE UserID = " + userId);
				String rating;
				if (results.first())
				{
					rating = results.getString("Seller_Rating");
				}
				
				//get categories
				results = stmt.executeQuery("SELECT * FROM Item_Category WHERE ItemID = " + itemId);
				ArrayList<String> categoryList = new ArrayList<String>();
				while (results.first())
				{
					categoryList.add(results.getString("Category"));
				}
				
				//append categories
				for (String category : categoryList)
				{
					Element categoryName = doc.createElement("Category");
					categoryName.appendChild(doc.createTextNode(category));
					rootElement.appendChild(categoryName);
				}
				
				//append currently
				Element currentlyElem = doc.createElement("Currently");
				currentlyElem.appendChild(doc.createTextNode(currently));
				rootElement.appendChild(currentlyElem);
				
				//append buy price
				Element buyPriceElem = doc.createElement("Buy_Price");
				buyPriceElem.appendChild(doc.createTextNode(buyPrice));
				rootElement.appendChild(buyPriceElem);
				
				//append first bid
				Element firstBidElem = doc.createElement("First_Bid");
				firstBidElem.appendChild(doc.createTextNode(firstBid));
				rootElement.appendChild(firstBidElem);
				
				//append num of bids
				Element numOfBidsElem = doc.createElement("Number_of_Bids");
				numOfBidsElem.appendChild(doc.createTextNode(numberOfBids));
				rootElement.appendChild(numOfBidsElem);
				
				//get number of bids
				results = stmt.executeQuery("SELECT COUNT(*) FROM Bid WHERE ItemID = " + itemId);
				String numOfBids;
				if (results.first())
				{
					numOfBids = results.getString("COUNT(*)");
				}
				
				//get bids
				results = stmt.executeQuery("SELECT * FROM Bid WHERE ItemID = " + itemId);
				Element bidsElem = doc.createElement("Bids");
				rootElement.appendChild(bidsElem);
				while (results.next())
				{
					Element bidElem = doc.createElement("Bid");
					Element bidderElem = doc.createElement("Bidder");
					
					String bidder = results.getString("BidderID");
					
					bidderElem.setAttribute(Bidder, value);
					bidElem.appendChild(bidderElem);
					bidsElem.appendChild(bidElem);
				}
				
			}
		} catch (SQLException ex) {
			System.out.println(ex);
		}
		
		catch (ParserConfigurationException ex) {
			System.out.println(ex);
		}
		
		return "";
	}
	
	public String echo(String message) {
		return message;
	}
	
	public String formatDate(String date)
	{
		String formattedDate = "";
		try{
			SimpleDateFormat newDateFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		    Date parsedDate = dateFormat.parse(date);
		    formattedDate = newDateFormat.format(parsedDate);
		}catch(Exception e){//this generic but you can control another types of exception
		  System.out.println(e);
		}
		return formattedDate;
	}

}
