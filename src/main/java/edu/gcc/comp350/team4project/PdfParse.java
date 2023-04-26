package edu.gcc.comp350.team4project;// Java Program to Extract Content from a PDF

// Importing java input/output classes
import java.io.File;
import java.io.FileInputStream;
// Importing Apache POI classes
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;

// Class
public class PdfParse {

    // Main driver method
    public static void main(String[] args) throws Exception
    {
        System.setProperty("java.library.path", "C:\\ffmpeg-2023-04-24-git-2aad9765ef-full_build\\bin");

        // Create a content handler
        BodyContentHandler contenthandler
                = new BodyContentHandler();

        // Create a file in local directory
        File f = new File("Biology.pdf");

        // Create a file input stream
        // on specified path with the created file
        FileInputStream fstream = new FileInputStream(f);

        // Create an object of type Metadata to use
        Metadata data = new Metadata();

        // Create a context parser for the pdf document
        ParseContext context = new ParseContext();

        // PDF document can be parsed using the PDFparser
        // class
        PDFParser pdfparser = new PDFParser();

        // Method parse invoked on PDFParser class
        pdfparser.parse(fstream, contenthandler, data,
                context);

        // Printing the contents of the pdf document
        // using toString() method in java
        System.out.println("Extracting contents :"
                + contenthandler.toString());
    }
}
