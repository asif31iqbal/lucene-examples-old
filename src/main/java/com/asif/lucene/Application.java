package com.asif.lucene;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;


public class Application {

    public static void main(String[] args) throws Exception {
        Indexer indexer = new Indexer("index");
        indexer.createIndex("data");
        indexer.close();
        
        Searcher searcher = new Searcher("index");
        
        testSearch(searcher, "bangladesh");
//        testSearch(searcher, "bangladesh AND asif");
//        testSearch(searcher, "bangladesh OR asif");
//        testSearch(searcher, "+bangladesh -asif");
//        testSearch(searcher, "-bangladesh -asif");
//        testSearch(searcher, "+content:bangladesh +name:three.txt");
//        //testSearchMultiField(searcher, "name:asif three gheu");
//        testSearch(searcher, "\"asif three    miu\"");
        
        //testSearchMultiField(searcher, "content:bangladesh AND name:\"asif iqbal three miu gheu\"");
        testPrefix(searcher, "asif iqbal");
        testBoolean(searcher, "bangladesh", "asif iqbal");
        testExact(searcher, "asif iqbal three miu gheu");
    }    
    
    public static void testSearch(Searcher searcher, String searchQuery) throws Exception {
        TopDocs hits = searcher.search(searchQuery, 5);
        
        System.out.println("Results for query: " + searchQuery);
        
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.getDocument(scoreDoc.doc);
            System.out.println("File: " + scoreDoc.doc + " " + doc.get(Constants.NAME) + " " +doc.get(Constants.PATH));
        }
    }
    
    public static void testSearchMultiField(Searcher searcher, String searchQuery) throws Exception {
        TopDocs hits = searcher.searchMultiField(searchQuery, 5);
        
        System.out.println("Results for query: " + searchQuery);
        
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.getDocument(scoreDoc.doc);
            System.out.println("File: " + scoreDoc.doc + " " + doc.get(Constants.NAME) + " " + doc.get(Constants.PATH));
        }
    }
    
    public static void testPrefix(Searcher searcher, String searchQuery) throws Exception {
        TopDocs hits = searcher.searchPrefix(searchQuery, 5);
        
        System.out.println("Results for query: " + searchQuery);
        
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.getDocument(scoreDoc.doc);
            System.out.println("File: " + scoreDoc.doc + " " + doc.get(Constants.NAME) + " " + doc.get(Constants.PATH));
        }
    }
    
    public static void testBoolean(Searcher searcher, String searchQueryContent, String searchQueryName) throws Exception {
        TopDocs hits = searcher.searchBoolean(searchQueryContent, searchQueryName, 5);
        
        System.out.println("Results for query: " + "boolean");
        
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.getDocument(scoreDoc.doc);
            System.out.println("File: " + scoreDoc.doc + " " + doc.get(Constants.NAME) + " " + doc.get(Constants.PATH));
        }
    }
    
    public static void testExact(Searcher searcher, String searchQuery) throws Exception {
        TopDocs hits = searcher.searchExact(searchQuery, 5);
        
        System.out.println("Results for query: " + searchQuery);
        
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.getDocument(scoreDoc.doc);
            System.out.println("File: " + scoreDoc.doc + " " + doc.get(Constants.NAME) + " " + doc.get(Constants.PATH));
        }
    }

}
