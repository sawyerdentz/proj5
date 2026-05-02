/*
This program decompresses files using hashing techniques. The program reads in a file specified by a name given by the user through a command line interface.
The program generates a decompressed text file using an ideal hashing technique with the size also being dynamically adjusted. A log file is produced with key metrics.
Created by Sawyer Dentz and Cavin Nguyen
Last modified: 5/1/2026
*/

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Decompress {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            // check that the user inputted a filename in argument
            String filename = "";
            if (args.length == 1) {
                filename = args[0];
            }
            File f = new File(filename);

            boolean runAgain;
            do {
                // make sure the file exists, if not prompt user for new filename
                while (!f.exists() || !f.isFile() || !filename.substring(filename.length()-4).equals(".zzz")) {
                    System.out.print("Please input a valid filename:\n>> ");
                    filename = sc.nextLine();
                    f = new File(filename);
                }

                // create hash table
                ArrayList<String> table = new ArrayList<>();

                // initialize log data
                long start = 0;
                long end = 0;
                int timesDoubled = 0;

                // create output file
                try (PrintWriter output = new PrintWriter(filename.substring(0,filename.length()-4))) {
                
                    // read input file
                    ObjectInputStream in = null;
                    try {
                        in = new ObjectInputStream(new FileInputStream(f));
                        // loop through file
                        try {
                            start = System.currentTimeMillis();
                            // get size of initial dictionary
                            int dictSize = in.readInt();
                            // loop through initial dict and add entries to table
                            for (int i = 0; i < dictSize; i++) {
                                String entry = in.readUTF();
                                table.add(entry);
                            }

                            // decompress data
                            int previousKey = -1;
                            while (true) { 
                                int key = in.readInt();
                                // if the key is not in the table
                                if (key > table.size() - 1) {
                                    // add new code to table and append to output file
                                    table.add(table.get(previousKey) + table.get(previousKey).charAt(0));
                                    output.append(table.get(key));
                                    previousKey = key;

                                }
                                // if the key is in the table
                                else {
                                    // append the code to the output file
                                    output.append(table.get(key));
                                    // if this is not the first code
                                    if (previousKey != -1) {
                                        table.add(table.get(previousKey) + table.get(key).charAt(0));
                                    }
                                    previousKey = key;
                                }
                            }
                            
                        }
                        catch (EOFException e) {
                            // reached end of file
                            end = System.currentTimeMillis();
                        }
                    }
                    catch (FileNotFoundException e) {
                        System.out.println(e.getMessage());
                        System.exit(1);
                    }
                    catch (IOException e) {
                        System.out.println(e.getMessage());
                        System.exit(1);
                    }
                    finally { 
                        if (in != null) { 
                            try { 
                                in.close();
                            }
                            catch(IOException e) { 
                                System.out.println("Error Closing");
                            }
                        }
                    }
                }
                catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
                // log data
                try (PrintWriter outputLog = new PrintWriter(new FileOutputStream(filename.substring(0,filename.length()-4) + ".log"))) {
                    outputLog.println("Decompression for file " + filename);
                    outputLog.println("Decompression took " + ((float) (end - start)/1000.0) + " seconds.");
                    outputLog.println("The table was doubled " + timesDoubled + " times.");
                }
                catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
                // prompt user to run again
                System.out.print("Would you like to run again? (y for yes, n for no)\n>> ");
                String response = sc.nextLine();
                if (response.equalsIgnoreCase("y")) {
                    runAgain = true;
                    System.out.print("Please input a valid filename:\n>> ");
                    filename = sc.nextLine();
                    f = new File(filename);
                }
                else {
                    runAgain = false;
                }
            } while (runAgain);
        }
    }   
}