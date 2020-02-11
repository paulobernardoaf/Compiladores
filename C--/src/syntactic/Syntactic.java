package syntactic;

import categories.CategoryList;
import lexic.Lexical;
import token.Token;

import java.io.IOException;

public class Syntactic {

    private Lexical lexicalAnalyzer;

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

    }

}
