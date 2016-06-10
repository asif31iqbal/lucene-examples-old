package com.asif.lucene;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class Indexer {
    
    /** Indexed, tokenized, not stored. */
    public static final FieldType CONTENT_FIELD_TYPE = new FieldType();

    /** Indexed, tokenized, stored. */
    public static final FieldType ID_FIELD_TYPE = new FieldType();
    
    /**
     * This is just to demonstrate the use of field types.
     * In real life, built in types like TextField would be used
     */
    static {
        CONTENT_FIELD_TYPE.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
        CONTENT_FIELD_TYPE.setTokenized(true);
        CONTENT_FIELD_TYPE.freeze();

        ID_FIELD_TYPE.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
        ID_FIELD_TYPE.setTokenized(false);
        ID_FIELD_TYPE.setStored(true);
        ID_FIELD_TYPE.freeze();
    }
    
    private IndexWriter writer;

    public Indexer(String indexDirectoryPath) throws Exception{
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
       Field contentField = new Field(Constants.CONTENT, new FileReader(file), CONTENT_FIELD_TYPE);
       //index file name
       Field fileNameField = new Field(Constants.NAME, "asif iqbal three miu gheu", ID_FIELD_TYPE);
       //index file path
       Field filePathField = new Field(Constants.PATH, file.getCanonicalPath(), ID_FIELD_TYPE);

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
