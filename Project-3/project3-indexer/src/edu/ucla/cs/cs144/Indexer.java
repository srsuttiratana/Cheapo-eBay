package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
    
    private IndexWriter indexWriter = null;
    
    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            
            //create new IndexWriter if 'create' is true
            if(create)
            {
            	config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            }
            
            else	//append to existing IndexWriter if it exists, else create
            {
            	config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            }
            
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
   }
    
    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }
    
    public void indexItem(int itemId, String name, String desc, String categories) throws IOException {
        IndexWriter writer = getIndexWriter(false);
        Document doc = new Document();
        doc.add(new StringField("ItemID", String.valueOf(itemId), Field.Store.YES));
        doc.add(new StringField("Name", name, Field.Store.YES));
        doc.add(new StringField("Description", desc, Field.Store.NO));
        doc.add(new StringField("Category", categories, Field.Store.NO));
        String fullSearchableText = itemId + " " + name + " " + desc + " " + categories;
        doc.add(new TextField("Content", fullSearchableText, Field.Store.NO));
        writer.addDocument(doc);
    }
 
    public void rebuildIndexes() {

        Connection conn = null;
        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
	
	try {
		getIndexWriter(true);
		Statement s = conn.createStatement() ;
		String sql_stmt = "SELECT Item.ItemID, Item.Name, Item.Description, Cat.Categories "
			+ "FROM (SELECT ItemID, GROUP_CONCAT(Item_Category.Category SEPARATOR ' ') AS Categories "
			+ "FROM Item_Category GROUP BY ItemID) AS Cat "
			+ "INNER JOIN Item " 
			+ "ON Item.ItemID = Cat.ItemID"
			;
	
		ResultSet fetched_items = s.executeQuery(sql_stmt);
	
		while(fetched_items.next())
		{
			indexItem(fetched_items.getInt("ItemID"), fetched_items.getString("Name"), fetched_items.getString("Description"), fetched_items.getString("Categories"));
		}
		closeIndexWriter();
	    conn.close();
	} catch (Exception ex) {
	    System.out.println(ex);
	}
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
