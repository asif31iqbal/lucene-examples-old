package com.asif.lucene;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class Indexer2 {
    
    private IndexWriter writer;
    
    public Indexer2(String indexDirectoryPath) throws Exception{
        //this directory will contain the indexes
        Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));

        //create the indexer
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        // Clear any existing index in the index directory
        config.setOpenMode(OpenMode.CREATE);
        writer = new IndexWriter(indexDirectory, config);
     }

     public void close() throws Exception{
        writer.close();
     }

     private Document getDocument(File file) throws Exception{
        Document document = new Document();

        FieldType contentFiledType = new FieldType(TextField.TYPE_STORED);
        contentFiledType.setTokenized(true);
        
        //index file contents
        Field contentField = new TextField(Constants.CONTENT, new FileReader(file));
        //index file name
        Field fileNameField = new StringField(Constants.NAME, file.getName(), Store.YES);
        //index file path
        Field filePathField = new StringField(Constants.PATH, file.getCanonicalPath(), Store.YES);

        document.add(contentField);
        document.add(fileNameField);
        document.add(filePathField);

        return document;
     }   
     
     private void indexFile(File file) throws Exception{
        System.out.println("Indexing " + file.getCanonicalPath());
        Document document = getDocument(file);
        writer.addDocument(document);
     }

     public int createIndex(String dataDirPath) throws Exception{
        //get all files in the data directory
        File[] files = new File(dataDirPath).listFiles();

        for (File file : files) {
            if(!file.isDirectory()
                && !file.isHidden()
                && file.exists()
                && file.canRead()) {
              indexFile(file);
           }
        }
        return writer.numDocs();
     }
}
