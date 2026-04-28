import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

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
        HashTableChain<String, Integer> table = new HashTableChain<>(tableSize);

        try {
            // loop through the first time to initialize the dictionary with characters
            int currentValue = 0;
            ArrayList<String> initialDict = new ArrayList<>();

            try (FileReader input = new FileReader(f)) {
                char ch;
                while ((ch = (char)input.read()) != -1) {
                    // increase currentValue for every character add to the table
                    if (table.get(ch) == null) {
                        String s = String.valueOf(ch);
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
                char current;
                // loop through file
                while ((current = (char) input.read()) != -1) {
                    longestString += current;
                    // if the longestString is not in the table, add it with the current value
                    if (table.get(longestString) == null) {
                        table.put(longestString, currentValue);
                        currentValue++;

                        // add longest value without last char to compressed file
                        Integer previousValue = table.get(longestString.substring(0, longestString.length() - 1));
                        if (previousValue == null) {
                            throw new IOException("Missing dictionary entry for prefix: " + longestString.substring(0, longestString.length() - 1));
                        }
                        out.writeInt(previousValue);

                        // reset longest string to current char
                        longestString = String.valueOf(current);
                    }
                }
                // add last string after looping through whole file
                out.writeInt(table.get(longestString));
            }

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
