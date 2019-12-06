package lexic;

import categories.CategoryList;
import categories.SeparatorList;
import token.Token;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Lexical {


    private int linePosition = 0, charPosition = 0;
    private ArrayList<Character> separators = new SeparatorList().getSeparatorList();
    private ArrayList<Character> separatorsButToken = new SeparatorList().getSeparatorButTokenList();
    private ArrayList<Character> separatorsButComparators = new SeparatorList().getSeparatorButComparator();
    private BufferedReader file;
    private String line = "";
    private boolean fileEOF = false;

    public Lexical(BufferedReader file) {
        this.file = file;
    }

    public char nextChar(String line) {
        char c = line.charAt(this.charPosition);
        this.charPosition += 1;
        return c;
    }

    // Function that return one token ( will be used inside getTokens() )
    public Token nextToken() {

        StringBuilder lexeme = new StringBuilder();

        if(fileEOF) {
            this.line = null;
            System.out.println("EOF");
            Token tk = new Token("", this.linePosition, this.charPosition );
            tk.setTokenCategory(CategoryList.TEOF);
            return tk;
        }

        if(separators.contains(line.charAt(this.charPosition))) {
            if(separatorsButToken.contains(line.charAt(this.charPosition))){
                lexeme.append(nextChar(line));
                if(this.charPosition < line.length() && separatorsButComparators.contains(lexeme.charAt(0)) && line.charAt(this.charPosition) == '='){
                    lexeme.append(nextChar(line));
                }
            } else {
                nextChar(line);
                return null;
            }
        } else {

            if(line.charAt(this.charPosition) == '\"') {
                lexeme.append(nextChar(line));
                while(!(line.charAt(this.charPosition) == '\"' && line.charAt(this.charPosition-1) != '\\')) {
                    lexeme.append(nextChar(line));
                }
                lexeme.append(nextChar(line));
            } else if(line.charAt(this.charPosition) == '#') {
               while(this.charPosition < line.length()) {
                   nextChar(line);
               }
               return null;
            } else {
                while(this.charPosition < line.length() && !separators.contains(line.charAt(this.charPosition))){
                    lexeme.append(nextChar(line));
                }
            }
        }

        return new Token(lexeme.toString(), this.linePosition, this.charPosition);
    }

    public String nextLine() throws IOException {

        this.line = file.readLine();

        if(line != null) {
            this.linePosition += 1;
            String format = "%04d  " + line;
            format = String.format(format, this.linePosition);
            System.out.println(format);
        } else if(!fileEOF){
            fileEOF = true;
            line =  " ";
        }

        return line;
    }

    public int getCharPosition() {
        return charPosition;
    }

    public void setCharPosition(int charPosition) {
        this.charPosition = charPosition;
    }

    public String getLine() {
        return line;
    }
}