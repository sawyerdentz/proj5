/*
This program reads out the data stored in a compressed file. It is designed to be used in order to verify that a file was compressed correctly.
It does not decompress the file.
Created by Sawyer Dentz and Cavin Nguyen
Last modified 5/1/2026
*/


import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class CompressedFileContentReader {
    public static void main(String[] args) {
        ObjectInputStream in = null;
        ArrayList<String> dict = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Input file to read: ");
            in = new ObjectInputStream(new FileInputStream(sc.nextLine()));
            try {
                int dictSize = in.readInt();
                System.out.println("Dictionary Size: " + dictSize);

                System.out.print("Dictionary Chars: ");
                for (int i = 0; i < dictSize; i++) {
                    String entry = in.readUTF();
                    dict.add(entry);
                    System.out.print(entry);
                }
                System.out.println();

                System.out.print("Integer Codes: ");
                while (true) {
                    System.out.print(in.readInt());
                    System.out.print(",");
                }
            } 
            catch (EOFException e) {
                System.out.println("\nEnd of file reached.");
            }
        }
        catch (FileNotFoundException e){
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
        sc.close();
    }
}
