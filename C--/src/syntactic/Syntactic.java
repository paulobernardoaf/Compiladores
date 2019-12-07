package syntactic;

import categories.CategoryList;
import lexic.Lexical;
import token.Token;

import java.io.IOException;

public class Syntactic {

    Lexical lexicalAnalyzer;


    public Syntactic(String args) {
        this.lexicalAnalyzer = new Lexical(args);
    }

    public void start() throws IOException {

        Token token;

        while(true) {
            token = lexicalAnalyzer.nextToken();
            if (token != null) {
                System.out.println(token.toString());
                if (token.getTokenCategory() == CategoryList.TEOF) {
                    return;
                }
            }
        }

//        while(lexicalAnalyzer.nextLine() != null) {
//            lexicalAnalyzer.setCharPosition(0);
//            while(lexicalAnalyzer.getLine() != null && lexicalAnalyzer.getCharPosition() < lexicalAnalyzer.getLine().length()) {
//                token = lexicalAnalyzer.nextToken();
//                if (token != null) {
//                    System.out.println(token.toString());
//                }
//            }
//        }

    }

}
