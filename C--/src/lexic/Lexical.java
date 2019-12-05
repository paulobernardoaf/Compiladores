package lexic;

import categories.SeparatorList;
import token.Token;

import java.util.ArrayList;

public class Lexical {


    private int linePosition = 0, charPosition = 0;
    private ArrayList<Character> separators = new SeparatorList().getSeparatorList();
    private ArrayList<Character> separatorsButToken = new SeparatorList().getSeparatorButTokenList();
    private ArrayList<Character> separatorsButComparators = new SeparatorList().getSeparatorButComparator();


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
                if(this.charPosition < line.length() && separatorsButComparators.contains(lexeme.charAt(0)) && line.charAt(this.charPosition) == '='){
                    lexeme.append(nextChar(line));
                }
            } else {
                nextChar(line);
                return null;
            }
        } else {

            // TODO: TENTAR MELHORAR O RECONHECIMENTO DE CONSTANTES STRING E COMENTARIOS
            if(line.charAt(this.charPosition) == '\"') {
                lexeme.append(nextChar(line));
                while(line.charAt(this.charPosition) != '\"') {
                    lexeme.append(nextChar(line));
                }
                lexeme.append(nextChar(line));
            } else if(line.charAt(this.charPosition) == '#') {
                while(this.charPosition < line.length()) {
                    lexeme.append(nextChar(line));
                }
            } else {
                while(this.charPosition < line.length() && !separators.contains(line.charAt(this.charPosition))){
                    lexeme.append(nextChar(line));
                }
            }
        }

        return new Token(lexeme.toString(), this.linePosition, this.charPosition);
    }

    public void addLinePosition() {
        this.linePosition += 1;
    }

    public int getLinePosition() {
        return linePosition;
    }

    public int getCharPosition() {
        return charPosition;
    }

    public void setCharPosition(int charPosition) {
        this.charPosition = charPosition;
    }
}