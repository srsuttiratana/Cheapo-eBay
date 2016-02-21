package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.text.SimpleDateFormat;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import edu.ucla.cs.cs144.AuctionSearchClient;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}
  	
  	public static class User
    {
		//TODO: Fill this out (and other helper classes if necessary)
    	String user_id;
    	String user_location;
    	String user_country;
    	String user_seller_rating;
    	String user_bidder_rating;
    	
    	//constructor
  	User(String user_id, String user_location, String user_country, String user_seller_rating, String user_bidder_rating)
  	{
  		this.user_id = user_id;
  		if(user_location.isEmpty()){
  			this.user_location = "";
  		}
  		else{
  			this.user_location = user_location;
  		}
  		if(user_country.isEmpty()){
  			this.user_country = "";
  		}
  		else{
  			this.user_country = user_country;
  		}
  		if(user_seller_rating.isEmpty()){
  			this.user_seller_rating = "";
  		}
  		else{
  			this.user_seller_rating = user_seller_rating;
  		}
  		if(user_bidder_rating.isEmpty()){
  			this.user_bidder_rating = "";
  		}
  		else{
  			this.user_bidder_rating = user_bidder_rating;
  		}
  	}
    }
	
	public static class Bid
	{
		public String bidder_id;
		public String bid_time;
		public String bid_amount;
		public String item_id;
		public String bidder_location;
		public String bidder_country;
		
		Bid(String bidder_id, String bid_time, String bid_amount, String item_id, String bidder_location, String bidder_country)
		{
			this.bidder_id = bidder_id;
			this.bid_time = bid_time;
			this.bid_amount = bid_amount;
			this.item_id = item_id;
			this.bidder_location = bidder_location;
			this.bidder_country = bidder_country;
		}
	}
	
	public static class Item
	{
		String item_id;
		String name;
		String currently;
		String buy_price;
		String first_bid;
		String num_of_bids;
		String location;
		String longitude;
		String latitude;
		String country;
		String started;
		String ends;
		String seller_id;
		String description;
		
		Item(String item_id, String name, String currently, String buy_price, String first_bid, String num_of_bids, String location,
				String longitude,
				String latitude,
				String country,
				String started,
				String ends,
				String seller_id,
				String description)
		{
					this.item_id = item_id;
					this.name = name;
					this.currently = currently;
					if(buy_price.isEmpty()){
						this.buy_price = "";
					}
					else{
						this.buy_price =  buy_price;
					}
					this.first_bid = first_bid;
					this.num_of_bids = num_of_bids;
					if(location.isEmpty())
						this.location = "";
					else{
						this.location = location;
					}
					if(longitude.isEmpty()){
						this.longitude = "";
					}
					else{
						this.longitude = longitude;
					}
					if(latitude.isEmpty()){
						this.latitude = "";
					}
					else{
						this.latitude = latitude;
					}
					if(country.isEmpty()){
						this.country = "";
					}
					else{
						this.country = country;
					}
					this.started = started;
					this.ends = ends;
					this.seller_id = seller_id;
					if(description.length() > 4000){
						this.description = description.substring(0, 4000);
					}
					else{
						this.description = description;
					}
		}
	}
	
	public static class Category
	{
		Set<String> category;
		String item_id;
		
		Category(Set<String> category, String item_id)
		{
			this.category = category;
			this.item_id = item_id;
		}
	}
  
  static String convert_To_SQL_DateTime(String XML_DateTime)
  {
      SimpleDateFormat old_format = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
      SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date old_formatted_date;
      String new_date = "";
      
      try
      {
      	old_formatted_date = old_format.parse(XML_DateTime);
      	new_date = new_format.format(old_formatted_date);
      }
      catch(ParseException e)
      {
      	System.out.println("Couldn't convert the date correctly.");
      }
      return new_date;   	
  }
  
  ////////////////////////END OF NEW IMPLEMENTED HELPER FUNCTIONS//////////////////////////////
  
  static class MyErrorHandler implements ErrorHandler {
      
      public void warning(SAXParseException exception)
      throws SAXException {
          fatalError(exception);
      }
      
      public void error(SAXParseException exception)
      throws SAXException {
          fatalError(exception);
      }
      
      public void fatalError(SAXParseException exception)
      throws SAXException {
          exception.printStackTrace();
          System.out.println("There should be no errors " +
                             "in the supplied XML files.");
          System.exit(3);
      }
      
  }
  
  /* Non-recursive (NR) version of Node.getElementsByTagName(...)
   */
  static Element[] getElementsByTagNameNR(Element e, String tagName) {
      Vector< Element > elements = new Vector< Element >();
      Node child = e.getFirstChild();
      while (child != null) {
          if (child instanceof Element && child.getNodeName().equals(tagName))
          {
              elements.add( (Element)child );
          }
          child = child.getNextSibling();
      }
      Element[] result = new Element[elements.size()];
      elements.copyInto(result);
      return result;
  }
  
  /* Returns the first subelement of e matching the given tagName, or
   * null if one does not exist. NR means Non-Recursive.
   */
  static Element getElementByTagNameNR(Element e, String tagName) {
      Node child = e.getFirstChild();
      while (child != null) {
          if (child instanceof Element && child.getNodeName().equals(tagName))
              return (Element) child;
          child = child.getNextSibling();
      }
      return null;
  }
  
  /* Returns the text associated with the given element (which must have
   * type #PCDATA) as child, or "" if it contains no text.
   */
  static String getElementText(Element e) {
      if (e.getChildNodes().getLength() == 1) {
          Text elementText = (Text) e.getFirstChild();
          return elementText.getNodeValue();
      }
      else
          return "";
  }
  
  /* Returns the text (#PCDATA) associated with the first subelement X
   * of e with the given tagName. If no such X exists or X contains no
   * text, "" is returned. NR means Non-Recursive.
   */
  static String getElementTextByTagNameNR(Element e, String tagName) {
      Element elem = getElementByTagNameNR(e, tagName);
      if (elem != null)
          return getElementText(elem);
      else
          return "";
  }
  
  /* Returns the amount (in XXXXX.xx format) denoted by a money-string
   * like $3,453.23. Returns the input if the input is an empty string.
   */
  static String strip(String money) {
      if (money.equals(""))
          return money;
      else {
          double am = 0.0;
          NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
          try { am = nf.parse(money).doubleValue(); }
          catch (ParseException e) {
              System.out.println("This method should work for all " +
                                 "money values you find in our data.");
              System.exit(20);
          }
          nf.setGroupingUsed(false);
          return nf.format(am).substring(1);
      }
  }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String queryId = request.getParameter("id");
    	String xmlData = AuctionSearchClient.getXMLDataForItemId(queryId);
    	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder docBuilder = null;
    	try{
    		docBuilder = docFactory.newDocumentBuilder();
    	}
    	catch (ParserConfigurationException e)
    	{
    		System.out.println("Parser configuration exception");
    	}
    	InputSource source = new InputSource(new StringReader(xmlData));
    	Document doc = null;
    	if (docBuilder == null)
    	{
    		System.out.println("DocumentBuilder is not initialized");
    		return;
    	}
        try {
            doc = docBuilder.parse(source);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file");
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        //System.out.println("Successfully parsed");
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        
        ///////////////////////NEW//////////////////////////
        
        //fetch the root of the XML Document
        Element root_element = doc.getDocumentElement();

        //start getting the "Items"
        Element[] items = getElementsByTagNameNR(root_element, "Item");

        	String item_id = items[0].getAttribute("ItemID");
        	String name = getElementTextByTagNameNR(items[0], "Name");
        	String currently = strip(getElementTextByTagNameNR(items[0], "Currently"));
        	String buy_price = strip(getElementTextByTagNameNR(items[0], "Buy_Price"));
        	String first_bid = strip(getElementTextByTagNameNR(items[0], "First_Bid"));
        	String num_of_bids = getElementTextByTagNameNR(items[0], "Number_Of_Bids");
        	String location = getElementTextByTagNameNR(items[0], "Location");
        	
        	Element e_location = getElementByTagNameNR(items[0], "Location");
        	String latitude = e_location.getAttribute("Latitude");
        	String longitude = e_location.getAttribute("Longitude");
        	
        	String country = getElementTextByTagNameNR(items[0], "Country");
        	String started = convert_To_SQL_DateTime(getElementTextByTagNameNR(items[0], "Started"));
        	String ends = convert_To_SQL_DateTime(getElementTextByTagNameNR(items[0], "Ends"));
        	
        	//add to user_map
        	Element seller = getElementByTagNameNR(items[0], "Seller");
        	String seller_id = seller.getAttribute("UserID");
        	String seller_rating = seller.getAttribute("Rating");
        	
        	String description = getElementTextByTagNameNR(items[0], "Description");
        	
        	Element bids_parent = getElementByTagNameNR(items[0], "Bids");
        	Element[] bids = getElementsByTagNameNR(bids_parent, "Bid");
        	ArrayList<Bid> bidList = new ArrayList();
        	//TODO: get all the bids
        	for(Element bid : bids)
        	{
        		Element e_bidder = getElementByTagNameNR(bid, "Bidder");
        		String bidder_rating = e_bidder.getAttribute("Rating");
        		String bidder_id = e_bidder.getAttribute("UserID");
        		String bidder_loc = getElementTextByTagNameNR(e_bidder, "Location");
        		String bidder_country = getElementTextByTagNameNR(e_bidder, "Country");
        		
        		String bid_time = convert_To_SQL_DateTime(getElementTextByTagNameNR(bid, "Time"));
        		String bid_amount = strip(getElementTextByTagNameNR(bid, "Amount"));
        		
        		Bid bid_temp = new Bid(bidder_id, bid_time, bid_amount, item_id, bidder_loc, bidder_country);
        		bidList.add(bid_temp);
        	}
        	
        	//TODO: categories
        	Element[] categories = getElementsByTagNameNR(items[0], "Category");
        	ArrayList<String> categoryList = new ArrayList<String>();
        	for(Element category : categories)
        	{
        		String cat_name = getElementText(category);
        		categoryList.add(cat_name);
        	}
        	
        	request.setAttribute("ItemID", item_id);
        	request.setAttribute("Name", name);
        	request.setAttribute("Categories", categoryList);
        	request.setAttribute("Currently", currently);
        	request.setAttribute("Buy_Price", buy_price);
        	request.setAttribute("Started", started);
        	request.setAttribute("Ends", ends);
        	request.setAttribute("Description", description);
        	request.setAttribute("First_Bid", first_bid);
        	request.setAttribute("Number_of_Bids", num_of_bids);
        	request.setAttribute("Bids", bidList);
        	request.setAttribute("Location", location);
        	request.setAttribute("Latitude", latitude);
        	request.setAttribute("Longitude", longitude);
        	request.setAttribute("seller_id", seller_id);
        	request.setAttribute("seller_rating", seller_rating);
        	
        	request.getRequestDispatcher("/itemResult.jsp").forward(request, response);
    }
}
