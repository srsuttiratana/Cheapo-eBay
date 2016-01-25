/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

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


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    ///////////////////////////////////NEW HELPER IMPLEMENTATIONS//////////////////////////////////////
    //store the User ID as the key and User
  	static Map<String, User> user_map = new HashMap<String, User>();
  	
  	//store the bids using a key string of bidder_id + time + item_id
  	static Map<String, Bid> bid_map = new HashMap<String, Bid>();
  	
  	//store the items with item_id as the key string
  	static Map<String, Item> item_map = new HashMap<String, Item>();
  	
  	//store the Item ID as the key and a distinct Set of Categories
  	static Map<String, Set<String>> category_map = new HashMap<String, Set<String>>();
  	
  	//store the bid keys so that we know whether there are duplicate bids
  	//optional
  	static Set<String> bid_set = new HashSet<String>();
  	
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
      		this.user_location = user_location;
      		this.user_country = user_country;
      		this.user_seller_rating = user_seller_rating;
      		this.user_bidder_rating = user_bidder_rating;
      	}
      	
      	public void change_location(String location)
      	{
      		user_location = location;
      	}
      	
      	public void change_country(String country)
      	{
      		user_country = country;
      	}
      	
      	public void change_seller_rating(String rating)
      	{
      		user_seller_rating = rating;
      	}
      	
      	public void change_bidder_rating(String rating)
      	{
      		user_bidder_rating = rating;
      	}
      	
      	public void write_to_user_stream(BufferedWriter user) throws IOException
      	{
      		String row = String.format("%s |*| %s |*| %s |*| %s |*| %s\n", 
      				user_id,
                      user_location,
                      user_country,
                      user_seller_rating,
                      user_bidder_rating);
               
               user.write(row); 
      	}
      }
  	
  	public static class Bid
  	{
  		String bidder_id;
  		String bid_time;
  		String bid_amount;
  		String item_id;
  		
  		Bid(String bidder_id, String bid_time, String bid_amount, String item_id)
  		{
  			this.bidder_id = bidder_id;
  			this.bid_time = bid_time;
  			this.bid_amount = bid_amount;
  			this.item_id = item_id;
  		}
  		
  		public void write_to_bid_stream(BufferedWriter bid) throws IOException
      	{
      		String row = String.format("%s |*| %s |*| %s |*| %s\n", 
      				  bidder_id,
                      bid_time,
                      bid_amount,
                      item_id);
               
               bid.write(row); 
      	}
  	}
	
	public static class Item
	{
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
		
		Item(String item_id, String currently, String buy_price, String first_bid, String num_of_bids, String location,
				String longitude,
				String latitude,
				String country,
				String started,
				String ends,
				String seller_id,
				String description)
		{
					this.item_id = item_id;
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
		
		public void write_to_item_stream(BufferedWriter item) throws IOException
      	{
      		String row = String.format("%s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s |*| %s\n", 
      				item_id,
      				currently,
      				buy_price,
      				first_bid,
      				num_of_bids,
      				location,
      				longitude,
      				latitude,
      				country,
      				started,
      				ends,
      				seller_id,
      				description);
               
               item.write(row); 
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
		
		public void write_to_category_stream(BufferedWriter cat) throws IOException
      	{
			for(String c : category)
			{
				String row = String.format("%s |*| %s\n", 
      				  c, item_id);
               
               cat.write(row); 
			}
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
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
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
        	
        	item_map.put(item_id, new Item(item_id, currently, buy_price, first_bid, num_of_bids, 
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
        			User u_temp = user_map.get(bid_key);
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



        //////////////////////END OF NEW////////////////////
        
        
        /**************************************************************/
        
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
        
        //TODO: Store currentFile's contents into data files for SQL
    }
}
