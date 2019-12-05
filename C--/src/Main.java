import syntactic.Syntactic;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            System.out.println("No path to files found in arguments");
        }
        for(String path : args) {
            System.out.println(" ##### START ##### \n");
            Syntactic syntacticAnalyzer = new Syntactic(path);
            syntacticAnalyzer.start();
            System.out.println("\n\n ##### END ##### \n");
        }
    }
}
