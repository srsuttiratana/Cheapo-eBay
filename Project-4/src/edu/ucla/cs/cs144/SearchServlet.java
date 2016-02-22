package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ucla.cs.cs144.AuctionSearchClient;
import edu.ucla.cs.cs144.SearchResult;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String q = request.getParameter("q");
        String num_results_to_skip = request.getParameter("numResultsToSkip");
        String num_results_to_return = request.getParameter("numResultsToReturn");
        int numResultsToSkip = 0;
        int numResultsToReturn = 20;
        if(num_results_to_skip != "")
        {
			try
			{
				numResultsToSkip = Integer.parseInt(num_results_to_skip);
			}
			catch(NumberFormatException e)
			{
				numResultsToSkip = 0;
				num_results_to_skip = "0";
			}
        }
        if(num_results_to_return != "")
        {
			try
			{
				numResultsToReturn = Integer.parseInt(num_results_to_return);
			}
			catch(NumberFormatException e)
			{
				numResultsToReturn = 20;
				num_results_to_return = "20";
			}
        }
        
        AuctionSearchClient search_client = new AuctionSearchClient();
        SearchResult[] results = search_client.basicSearch(q,numResultsToSkip,numResultsToReturn);
		//SearchResult[] results = search_client.basicSearch("camera",0,20);
        
        request.setAttribute("results", results);
        request.setAttribute("q", q);
		//request.setAttribute("q", "camera");
        request.setAttribute("numResultsToSkip", num_results_to_skip);
        request.setAttribute("numResultsToReturn", num_results_to_return);
		//request.setAttribute("numResultsToSkip", numResultsToSkip);
		//request.setAttribute("numResultsToReturn", numResultsToReturn);
        request.getRequestDispatcher("/searchResults.jsp").forward(request, response);
    }
}
