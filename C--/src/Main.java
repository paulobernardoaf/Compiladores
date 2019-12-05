import syntactic.Syntactic;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        System.out.println(args[0]);
        for(String path : args) {
            System.out.println(" ##### START ##### \n");
            Syntactic syntacticAnalyzer = new Syntactic(path);
            try {
                syntacticAnalyzer.readFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("\n\n ##### END ##### \n");
        }
    }
}
