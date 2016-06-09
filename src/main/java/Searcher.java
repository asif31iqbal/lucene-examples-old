import java.io.File;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;


public class Searcher {
    private IndexSearcher searcher = null;
    private QueryParser parser = null;

    /** Creates a new instance of SearchEngine */
    public Searcher(String indexDir) throws Exception {
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(indexDir))));
        parser = new QueryParser("content", new StandardAnalyzer());
    }

    public TopDocs search(String queryString, int n) throws Exception {
        Query query = parser.parse(queryString);
        return searcher.search(query, n);
    }

    public Document getDocument(int docId) throws Exception {
        return searcher.doc(docId);
    }

}
