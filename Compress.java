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
            // implement this...

        // create FileReader object
        try {
            FileReader input = new FileReader(f);

            
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
