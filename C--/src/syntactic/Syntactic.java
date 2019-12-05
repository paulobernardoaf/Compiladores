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

    // Function that returns all tokens inside one line
    public void getTokens(String line) {
        lexicalAnalyzer.setCharPosition(0);
        while(lexicalAnalyzer.getCharPosition() < line.length()) {
            Token token = lexicalAnalyzer.nextToken(line);
            if (token != null) {
                System.out.println(token.toString());
            }
        }
    }

    public void readFile() throws IOException {

        String line;

        while((line = this.file.readLine()) != null)
        {
            lexicalAnalyzer.addLinePosition();
            String format = "%04d  " + line;
            format = String.format(format, lexicalAnalyzer.getLinePosition());
            System.out.println(format);
            getTokens(line);
        }
    }


}
