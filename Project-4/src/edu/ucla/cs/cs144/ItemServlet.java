package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import edu.ucla.cs.cs144.AuctionSearchClient;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String queryId = request.getParameter("id");
    	String xmlData = AuctionSearchClient.getXMLDataForItemId(queryId);
    	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    	InputSource source = new InputSource(new StringReader(xmlData));
    	Document doc = null;
        try {
            doc = builder.parse(source);
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
        System.out.println("Successfully parsed");
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        
        ///////////////////////NEW//////////////////////////
        
        //fetch the root of the XML Document
        Element root_element = doc.getDocumentElement();

        //start getting the "Items"
        Element[] items = getElementsByTagNameNR(root_element, "Item");

        //construct Item objects, etc.
        for(Element item : items)
        {
        	/*
        	 	String item_id;
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
        	 */
        	String item_id = item.getAttribute("ItemID");
        	String name = getElementTextByTagNameNR(item, "Name");
        	String currently = strip(getElementTextByTagNameNR(item, "Currently"));
        	String buy_price = strip(getElementTextByTagNameNR(item, "Buy_Price"));
        	String first_bid = strip(getElementTextByTagNameNR(item, "First_Bid"));
        	String num_of_bids = getElementTextByTagNameNR(item, "Number_Of_Bids");
        	String location = getElementTextByTagNameNR(item, "Location");
        	
        	Element e_location = getElementByTagNameNR(item, "Location");
        	String latitude = e_location.getAttribute("Latitude");
        	String longitude = e_location.getAttribute("Longitude");
        	
        	String country = getElementTextByTagNameNR(item, "Country");
        	String started = convert_To_SQL_DateTime(getElementTextByTagNameNR(item, "Started"));
        	String ends = convert_To_SQL_DateTime(getElementTextByTagNameNR(item, "Ends"));
        	
        	//add to user_map
        	Element seller = getElementByTagNameNR(item, "Seller");
        	String seller_id = seller.getAttribute("UserID");
        	String seller_rating = seller.getAttribute("Rating");
        	
        	User u = user_map.get(seller_id);
        	if(u != null)	//seller already exists in user_map
        	{
        		u.user_seller_rating = seller_rating;
        		user_map.put(seller_id, u);
        	}
        	else
        	{	//User Constructor:
        		//User(String user_id, String user_location, String user_country, 
        		//String user_seller_rating, String user_bidder_rating)
        		user_map.put(seller_id, new User(seller_id, "", "", seller_rating, ""));
        	}
        	
        	String description = getElementTextByTagNameNR(item, "Description");
        	
        	item_map.put(item_id, new Item(item_id, name, currently, buy_price, first_bid, num_of_bids, 
        			location, longitude, latitude, country, started, ends, seller_id, description));
        	
        	//TODO: bids
        	/*
        	 	String bidder_id;
        		String bid_time;
        		String bid_amount;
        		String item_id;
        	 */
        	
        	Element bids_parent = getElementByTagNameNR(item, "Bids");
        	Element[] bids = getElementsByTagNameNR(bids_parent, "Bid");
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
        		
        		//add to bid_map
        		String bid_key = bidder_id + bid_time + item_id;
        		if(!bid_map.containsKey(bid_key))
        		{
        			bid_map.put(bid_key, new Bid(bidder_id, bid_time, bid_amount, item_id));
        		}
        		
        		//add to user_map
        		if(!user_map.containsKey(bidder_id))
        		{
        			//User(String user_id, String user_location, String user_country, 
        			//String user_seller_rating, String user_bidder_rating)
        			user_map.put(bidder_id, new User(bidder_id, bidder_loc, bidder_country, "", bidder_rating));
        		}
        		else	//update existing user
        		{
        			User u_temp = user_map.get(bidder_id);
        			u_temp.user_bidder_rating = bidder_rating;
        			u_temp.user_country = bidder_country;
        			u_temp.user_location = bidder_loc;
        			user_map.put(bidder_id, u_temp);
        		}
        	}
        	
        	//TODO: categories
        	Element[] categories = getElementsByTagNameNR(item, "Category");
        	for(Element category : categories)
        	{
        		String cat_name = getElementText(category);
        		if(!category_map.containsKey(item_id))
        		{
        			Set<String> c_set = new HashSet<String>();
        			c_set.add(cat_name);
        			category_map.put(item_id, c_set);
        		}
        		else
        		{
        			Set<String> c_set = category_map.get(item_id);
        			c_set.add(cat_name);
        			category_map.put(item_id, c_set);
        		}
        	}
        }
    }
}
