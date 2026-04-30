import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;


public class Compress {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            // check that the user inputted a filename in argument
            String filename = "";
            if (args.length == 1) {
                filename = args[0];
            }


            // make sure the file exists, if not prompt user for new filename
            File f = new File(filename);
            while (!f.exists() || !f.isFile()) {
                System.out.print("Please input a valid filename:\n>> ");
                filename = sc.nextLine();
                f = new File(filename);
            }
       
            // dynamically create dictionary of an optimal size
            long fileSize = f.length();
            int tableSize = BigInteger.valueOf((int) fileSize / 8).nextProbablePrime().intValue();
            // if (fileSize < 10000) {
            //     tableSize = 101;
            // } else if (fileSize >= 1000 && fileSize < 1000000) {
            //     tableSize = 1009;
            // } else {
            //     tableSize = 10007;
            // }


            // create hash table
            HashTableChain<String, Integer> table = new HashTableChain<>(tableSize);


            try {
                // loop through the first time to initialize the dictionary with characters
                int currentValue = 0;
                ArrayList<String> initialDict = new ArrayList<>();
                long start = System.currentTimeMillis();
                try (FileReader input = new FileReader(f)) {
                    int ch;
                    while ((ch = input.read()) != -1) {
                        char character = (char) ch;
                        // increase currentValue for every character add to the table
                        if (table.get(String.valueOf(character)) == null) {
                            String s = String.valueOf(character);
                            table.put(s, currentValue);
                            initialDict.add(s);
                            currentValue++;
                        }
                    }
                }


                // create ObjectOutputStream
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename + ".zzz"));
                    FileReader input = new FileReader(f)) {


                    // add initial dictionary to compressed file
                    // write number of initial entries followed by each string in insertion order
                    out.writeInt(initialDict.size());
                    for (String s : initialDict) {
                        out.writeUTF(s);
                    }


                    // loop through again and compress string. add output to new file.
                    String longestString = "";
                    int ch;
                    // loop through file
                    while ((ch = input.read()) != -1) {
                        char current = (char) ch;
                        longestString += current;
                        // if the longestString is not in the table, add it with the current value
                        if (table.get(longestString) == null) {
                            // add longest value without last char to compressed file
                            String prefix = longestString.substring(0, longestString.length() - 1);
                            Integer previousValue = table.get(prefix);
                            if (previousValue == null) {
                                throw new IOException("Missing dictionary entry for prefix: " + prefix);
                            }
                            out.writeInt(previousValue);


                            table.put(longestString, currentValue);
                            currentValue++;


                            // reset longest string to current char
                            longestString = "" + current;
                        }
                    }
                    // add last string after looping through whole file
                    if (!longestString.isEmpty()) {
                        out.writeInt(table.get(longestString));
                    }
                   
                }
                // get new time to determine how long it took for compression algorithm
                long end = System.currentTimeMillis();


                // log data into a separate file
                try (PrintWriter output = new PrintWriter(new FileOutputStream(filename + ".zzz.log"))) {
                    File compressedFile = new File(filename + ".zzz");
                    output.println("Compression of " + filename);
                    output.println("Compressed from " + fileSize/1024 + " Kilobytes to " + compressedFile.length()/1024 + " Kilobytes");
                    output.println("Compression took " + ((float) (end - start)/1000.0) + " seconds");
                    output.println("The dictionary contains " + table.getNumKeys() + " total entries");
                    output.println("The table was rehashed " + table.getRehashed() + " times");
                }


            } catch (FileNotFoundException e) {
                System.out.println("Error: file not found");
                e.printStackTrace();
                System.exit(1);
            } catch (EOFException e) {
                System.out.println("Error: end of file exception");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Error: IO exception");
                e.printStackTrace();
            }


            // close scanner
            sc.close();


        }
    }
}
