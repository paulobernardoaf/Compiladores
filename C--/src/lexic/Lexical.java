package lexic;

import categories.CategoryList;
import categories.SeparatorList;
import token.Token;

import java.io.*;
import java.util.ArrayList;

public class Lexical {


    private int linePosition = 0, charPosition = 0;
    private ArrayList<Character> separators = new SeparatorList().getSeparatorList();
    private ArrayList<Character> separatorsButToken = new SeparatorList().getSeparatorButTokenList();
    private ArrayList<Character> separatorsButComparators = new SeparatorList().getSeparatorButComparator();
    private BufferedReader file;
    private String line = "";
    private boolean fileEOF = false;

    public Lexical(String file) {
        try {
            this.file = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    private char nextChar(String line) {
        char c = line.charAt(this.charPosition);
        this.charPosition += 1;
        return c;
    }

    // Function that return one token
    public Token nextToken() {

        StringBuilder lexeme = new StringBuilder();

        if (this.line == null || this.charPosition >= this.line.length()) {
            this.charPosition = 0;
            try {
                this.line = file.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(line != null) {
                this.linePosition += 1;
                String format = "    %d  " + line;
                format = String.format(format, this.linePosition);
                System.out.println(format);
            } else if(!fileEOF){
                fileEOF = true;
                this.linePosition += 1;
                line =  " ";
            }
            return nextToken();
        }
        else {

            if(fileEOF) {
                this.line = null;
                String format = "    %d  EOF";
                format = String.format(format, this.linePosition);
                System.out.println(format);
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
                    return nextToken();
                }
            } else {

                if(line.charAt(this.charPosition) == '\"') {
                    lexeme.append(nextChar(line));
                    while(!(line.charAt(this.charPosition) == '\"' && line.charAt(this.charPosition-1) != '\\')) {
                        lexeme.append(nextChar(line));
                    }
                    lexeme.append(nextChar(line));
                } else if(line.charAt(this.charPosition) == '#') {
                    this.charPosition = line.length();
                    return nextToken();
                } else {
                    while(this.charPosition < line.length() && !separators.contains(line.charAt(this.charPosition))){
                        lexeme.append(nextChar(line));
                    }
                }

            }

            return new Token(lexeme.toString(), this.linePosition, this.charPosition);

        }

    }

}