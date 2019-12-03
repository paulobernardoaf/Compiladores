import lexic.Lexical;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        System.out.println(args[0]);
        Lexical lexicalAnalyzer = new Lexical(args[0]);
        try {
            lexicalAnalyzer.readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
