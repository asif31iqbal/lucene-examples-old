import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;


public class Application {

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        Indexer indexer = new Indexer("index");
        indexer.createIndex("data");
        indexer.close();
        
        Searcher searcher = new Searcher("index");
        String searchQuery = "bangladesh";
        TopDocs hits = searcher.search(searchQuery, 5);
        
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.getDocument(scoreDoc.doc);
            System.out.println("File: " + scoreDoc.doc + " " + doc.get(Constants.NAME) + doc.get(Constants.PATH));
        }
    }    

}
