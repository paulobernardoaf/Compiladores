package syntactic;

import lexic.Lexical;
import token.Token;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Syntactic {

    Lexical lexicalAnalyzer = new Lexical();
    private BufferedReader file;

    public Syntactic(String args) {
        try {
            this.file = new BufferedReader(new FileReader(args));
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        lexicalAnalyzer.readFile(this.file);
    }

}
