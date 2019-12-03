package lexic;

import categories.SeparatorList;
import token.Token;

import java.io.*;
import java.util.ArrayList;

public class Lexical {

    private BufferedReader file;
//    ArrayList<Token> tokens = new ArrayList<>();
    private int linePosition, charPosition;
    private ArrayList<Character> separators = new SeparatorList().getSeparatorList();
    private ArrayList<Character> separatorsButToken = new SeparatorList().getSeparatorButTokenList();


    public Lexical(String filePath) {
        try {
            this.file = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    public char nextChar(String line) {

        char c = line.charAt(this.charPosition);
        this.charPosition += 1;

        return c;
    }

    // Function that return one token ( will be used inside getTokens() )
    public Token nextToken(String line) {

        StringBuilder lexeme = new StringBuilder();

        if(separators.contains(line.charAt(this.charPosition))) {
            if(separatorsButToken.contains(line.charAt(this.charPosition))){
                lexeme.append(nextChar(line));
            } else {
                nextChar(line);
                return null;
            }
        } else {
            while(!separators.contains(line.charAt(this.charPosition))){
                lexeme.append(nextChar(line));
            }



        }


        return new Token(lexeme.toString(), this.linePosition, this.charPosition);
    }

    // Function that returns all tokens inside one line
    public void getTokens(String line) {
        for (this.charPosition = 0; this.charPosition< line.length();) {
                Token token = nextToken(line);
                if (token != null) {
                    System.out.println(token.toString());
                }
        }
    }

    public void readFile() throws IOException {

        String line;

        while((line = this.file.readLine()) != null)
        {
            this.linePosition += 1;
            getTokens(line);
        }


    }


}
