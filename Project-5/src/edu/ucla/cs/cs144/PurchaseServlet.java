package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.HashMap;

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

public class PurchaseServlet extends HttpServlet implements Servlet {
	public PurchaseServlet(){}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
		  try
		  {	
			  if (request.isSecure())
			  {
				  HttpSession session = request.getSession(true);
				  ItemServlet.Item current_item = (ItemServlet.Item)session.getAttribute("current_item");
				  String itemID = current_item.item_id;
				  String itemName = current_item.item_name;
				  String buyPrice = current_item.buy_price;
				  
				  request.setAttribute("ItemID", itemID);
				  request.setAttribute("Name", itemName);
				  request.setAttribute("Buy_Price", buyPrice);
				  request.setAttribute("Credit_Card_Number", request.getParameter("cardinput"));
				  request.setAttribute("Time", new Date());
				  request.getRequestDispatcher("/confirmation.jsp").forward(request, response);
			  }
		  }
		  catch (Exception e)
		  {
				HttpSession session = request.getSession(true);
				ItemServlet.Item current_item = (ItemServlet.Item)session.getAttribute("current_item");
				String itemID = current_item.item_id;
				String itemName = current_item.item_name;
				String buyPrice = current_item.buy_price;
				session.setAttribute("ItemTryingToPurchase", itemID);
				 
				request.setAttribute("ItemID", itemID);
				request.setAttribute("Name", itemName);
				request.setAttribute("Buy_Price", buyPrice);
				request.getRequestDispatcher("/creditCardInput.jsp").forward(request, response);
		  }
	  }
	  
	public void doPost(HttpServletRequest request,
                     HttpServletResponse response)
      throws ServletException, IOException {
			doGet(request, response);
	}
}