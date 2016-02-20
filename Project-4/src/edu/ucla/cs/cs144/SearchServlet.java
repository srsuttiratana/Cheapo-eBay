package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String q = request.getParameter("q");
        String num_results_to_skip = request.getParameter("numResultsToSkip");
        String num_results_to_return = request.getParameter("numResultsToReturn");
        int numResultsToSkip = 0;
        int numResultsToReturn = 0;
        if(num_results_to_skip != "")
        {
        	numResultsToSkip = Integer.parseInt(num_results_to_skip);
        }
        if(num_results_to_return != "")
        {
        	numResultsToReturn = Integer.parseInt(num_results_to_return);
        }
        
        AuctionSearchClient search_client = new AuctionSearchClient();
        SearchResult[] results = search_client.basicSearch(q,numResultsToSkip,numResultsToReturn);
        
        request.setAttribute("results", results);
        request.setAttribute("q", q);
        request.setAttribute("numResultsToSkip", num_results_to_skip);
        request.setAttribute("numResultsToReturn", num_results_to_return);
        request.getRequestDispatcher("/searchResults.jsp").forward(request, response);
    }
}
