package com.asif.lucene;
import java.io.File;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;


public class Searcher {
    private IndexSearcher searcher = null;

    /** Creates a new instance of SearchEngine */
    public Searcher(String indexDir) throws Exception {
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(indexDir))));
    }

    public TopDocs search(String queryString, int n) throws Exception {
        QueryParser parser = new QueryParser("content", new StandardAnalyzer());
        Query query = parser.parse(queryString);
        return searcher.search(query, n);
    }

    public Document getDocument(int docId) throws Exception {
        return searcher.doc(docId);
    }

    public TopDocs searchMultiField(String queryString, int n) throws Exception {
        MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"content", "path"}, new StandardAnalyzer());
        Query query = parser.parse(queryString);
        return searcher.search(query, n);
    }
    
    public TopDocs searchPrefix(String queryString, int n) throws Exception {        
        Term t = new Term("name", queryString);
        PrefixQuery query = new PrefixQuery(t);
        return searcher.search(query, n);
    }
    
    public TopDocs searchBoolean(String queryStringContent, String queryStringName, int n) throws Exception {        
        TermQuery tQuery = new TermQuery(new Term("content", queryStringContent));
        Term t = new Term("name", queryStringName);
        PrefixQuery pQuery = new PrefixQuery(t);
        
        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
        queryBuilder.add(tQuery, Occur.MUST); 
        queryBuilder.add(pQuery, Occur.MUST);
        BooleanQuery query = queryBuilder.build();
        
        return searcher.search(query, n);
    }
    
    public TopDocs searchExact(String queryString, int n) throws Exception {        
        TermQuery query = new TermQuery(new Term("name", queryString));
        return searcher.search(query, n);
    }
}
