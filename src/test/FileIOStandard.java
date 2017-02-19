package test;
// The "FileIO" class.
import java.awt.*;
import hsa.Console;
import java.io.*;

public class FileIOStandard
{
    static Console c;           // The output console

    public static void main (String[] args) throws IOException
    {
	c = new Console ();

	c.print ("File name to view: ");
	String fname = c.readLine ();

	FileReader fr = new FileReader (fname);
	BufferedReader filein = new BufferedReader (fr);

	String line;
	while ((line = filein.readLine ()) != null) // file has not ended
	    c.println (line); // display text
	filein.close (); // close file

	c.print ("File name to create: ");
	fname = c.readLine ();

	FileWriter fw = new FileWriter (fname);
	PrintWriter fileout = new PrintWriter (fw);


	for (int x = 1 ; x <= 100 ; x++) // save first 1000 integers
	    fileout.println (x); // write a value to file
	fileout.close (); // close file

    } // main method
} // FileIO class
