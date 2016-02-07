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
import java.sql.Statement;

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

public class AuctionSearch implements IAuctionSearch {
	
	private IndexSearcher searcher = null;
    private QueryParser parser = null;
	
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
		// TODO: Your code here!
		return new SearchResult[0];
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		return "";
	}
	
	public String echo(String message) {
		return message;
	}

}
