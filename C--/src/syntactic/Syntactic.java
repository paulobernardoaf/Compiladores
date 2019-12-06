package syntactic;

import lexic.Lexical;
import token.Token;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Syntactic {

    Lexical lexicalAnalyzer;


    public Syntactic(String args) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(args));
            this.lexicalAnalyzer = new Lexical(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    public void start() throws IOException {

        while(lexicalAnalyzer.nextLine() != null) {
            lexicalAnalyzer.setCharPosition(0);
            while(lexicalAnalyzer.getLine() != null && lexicalAnalyzer.getCharPosition() < lexicalAnalyzer.getLine().length()) {
                Token token = lexicalAnalyzer.nextToken();
                if (token != null) {
                    System.out.println(token.toString());
                }
            }
        }

    }

}
