package edu.ucla.cs.cs144;

import java.io.IOException;
import java.net.HttpURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.*;
import java.io.*;
import java.net.URLEncoder;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String baseURLSearch = "http://google.com/complete/search?output=toolbar&q=";
		response.setContentType("text/xml");
		
		String searchTerm = request.getParameter("q");
		String searchTermEncoded = URLEncoder.encode(searchTerm, "UTF-8");
		
		baseURLSearch += searchTermEncoded;
		
		URL baseURL = new URL(baseURLSearch);
		
		HttpURLConnection urlConnection = (HttpURLConnection) baseURL.openConnection();
		urlConnection.setRequestProperty("Accept-Charset", "UTF-8");

		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		
		String input = br.readLine();
		StringBuffer sb = new StringBuffer();
		while (input != null)
		{
			sb.append(input);
			input = br.readLine();
		}
		
		PrintWriter pw = response.getWriter();
		pw.println(sb.toString());
		//urlConnection.connect();
		
		//println(baseURLSearch);
		
    }
}