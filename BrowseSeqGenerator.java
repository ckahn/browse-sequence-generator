import java.io.*;
import java.util.regex.*;

public class BrowseSeqGenerator {

	BufferedReader reader = null;
	File saved_brs = null;
	BufferedWriter writer = null;
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.createGUI();
	}
	
	public void read(File hhc) throws IOException {
		reader = new BufferedReader(new FileReader(hhc));
	}
	
	public void setBSName(File brs) {
		saved_brs = brs;
	}
	
	/* 
	 * Creates the specified browse sequence (*.brs) file by
	 * creating the starting code, adding the necessary lines 
	 * from the table of contents (*.hhc) file, and adding the
	 * ending code.
	 */
	public boolean createBRS() throws IOException {
		writer = new BufferedWriter(new FileWriter(saved_brs));
		
		if (reader == null || writer == null) { return false; }
		
		// Add starting code to file
	    writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>"+"\n\r");
	    writer.write("<browsesequencelist version=\"1.0\">"+"\n\r");
	    writer.write("\t"+"<browsesequence name=\"Untitled\">"+"\n\r");
	    
	    // Add and format required lines from *.hhc file
	    Pattern pattern = Pattern.compile("<item name=\".*?\" link=\".*?\">");
	    String line = "";
	    while ((line = reader.readLine()) != null) {
	    	Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
			    line = line.trim();
			    writer.write("\t"+"\t"+line+"\n\r");
			    writer.write("\t"+"\t"+"</item>"+"\n\r");
			}
	    }
	    
	    // Add ending code to file
	    writer.write("\t"+"</browsesequence>"+"\n\r");
	    writer.write("</browsesequencelist>");
	    return true; // Return true if successful
	}
	
	public void close() throws IOException {
		if (writer != null) { writer.close(); }
		if (reader != null) { reader.close(); }
	}
}
