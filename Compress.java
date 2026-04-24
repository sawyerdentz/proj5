import java.util.Scanner;
import java.io.*;

public class Compress {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

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
        int tableSize;
        if (fileSize < 10000) {
            tableSize = 101;
        }
        else if (fileSize >= 1000 && fileSize < 1000000) {
            tableSize = 1009;
        }
        else {
            tableSize = 10007;
        }

        // create hash table
        HashTableChain<String, Integer> table = new HashTableChain<String, Integer>(tableSize);

        // create FileReader object
        try {
            // loop through the first time to initialize the dictionary with characters
            FileReader input = new FileReader(f);
            char ch;
            int currentValue = 0;
            while((ch = (char)input.read()) != -1) {
                // increase currentValue for every character add to the table
                if (table.get(ch) == null) {
                    table.put(String.valueOf(ch), currentValue);
                    currentValue++;
                }
            }
            input.close();

            // loop through again and compress string. add output to new file.
            // create ObjectOutputStream
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename + ".zzz"));

            input = new FileReader(f); // initialize new file to read again
            String longestString = "";
            char current;
            // loop through file
            while((current = (char)input.read()) != -1) {
                longestString += current;
                // if the longestString is not in the table, add it with the current value
                if (table.get(longestString) == null) {
                    table.put(longestString, currentValue);
                    currentValue++;

                    // add longest value without last char to compressed file
                    out.writeInt(table.get(longestString.substring(0,longestString.length()-1)));



                    // reset longest string to current char
                    longestString = String.valueOf(current);
                }
            }
            // add last string after looping through whole file
            out.writeInt(table.get(longestString));

            // close files
            input.close();
            out.close();


        }
        catch (FileNotFoundException e) {
            System.out.println("Error: file not found");
            System.exit(1);
        }
        catch (EOFException e) {
            System.out.println("Error: end of file exception");
        }
        catch (IOException e) {
            System.out.println("Error: IO exception");
        }

        // close scanner
        sc.close();

    }
}
