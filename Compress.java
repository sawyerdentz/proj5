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
            while((ch = (char)input.read()) != -1) {
                // increase currentValue for every character add to the table
                int currentValue = 0;
                if (table.get(ch) != null) {
                    table.put(String.valueOf(ch), currentValue);
                    currentValue++;
                }
            }
            input.close();

            // loop through again and compress string. add output to new file.

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

    }
}
