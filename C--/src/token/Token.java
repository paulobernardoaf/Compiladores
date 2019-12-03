package token;

import categories.CategoryList;
import categories.LexemeCategoryMap;

import java.util.Objects;

public class Token {

    private CategoryList tokenCategory;
    private int lineNumber, columnNumber;
    private String id;

    public Token(String lexeme, int lineNumber, int columnNumber) {
        this.id = lexeme;
        CategoryList cat = LexemeCategoryMap.MapLexemeCategory.get(lexeme);
        if(cat == null) {
            tokenCategory = getCategoryFromRegex(lexeme);
            if(tokenCategory == CategoryList.Tunknown) {
                System.out.println("categoria deu null para o lexema " + lexeme);
            }
        } else {
            tokenCategory = cat;
        }
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber - lexeme.length();
    }

    @Override
    public String toString() {
        String format = "          [%04d, %04d] (%04d, %20s) {%s}";
        return String.format(format, lineNumber, columnNumber, tokenCategory.ordinal(), tokenCategory.toString(), id);
    }

    public CategoryList getCategoryFromRegex(String lexeme) {
        CategoryList category = CategoryList.Tunknown;

        String regexId = "([a-z])([a-zA-Z]|[0-9]|_)*";
        String regexFuncId = "([A-Z])([a-zA-Z]|[0-9]|_)*";
        String regexCteInt = "[0-9]+";
        String regexCteFloat = "[0-9]+(\\.[0-9]+)?";
        String regexCteBool = "(true|false)";
        String regexCteChar = "'([a-z]|[A-Z]|[0-9]|[ /\\\\!@#$%&*()_\\-=+\\[\\]{}><?.;,\"'])'";
        String regexCteString = "\"([a-z]|[A-Z]|[0-9]|[ /\\\\!@#$%&*()_\\-=+\\[\\]{}><?.;,\"'])*\"";
//        String regexVecInt = "(int\\[)((([a-z])([a-zA-Z]|[0-9]|_)*)|([0-9]+))]";
//        String regexVecFloat = "(float\\[)((([a-z])([a-zA-Z]|[0-9]|_)*)|([0-9]+))]";
//        String regexVecBool = "(bool\\[)((([a-z])([a-zA-Z]|[0-9]|_)*)|([0-9]+))]";
//        String regexVecChar = "(char\\[)((([a-z])([a-zA-Z]|[0-9]|_)*)|([0-9]+))]";
//        String regexVecString = "(string\\[)((([a-z])([a-zA-Z]|[0-9]|_)*)|([0-9]+))]";

        if(lexeme.matches(regexId)) {
            category = CategoryList.TnameId;
        }
        else if(lexeme.matches(regexFuncId)) {
            category = CategoryList.TfuncId;
        }
        else if(lexeme.matches(regexCteInt)) {
            category = CategoryList.TcteInt;
        }
        else if(lexeme.matches(regexCteFloat)) {
            category = CategoryList.TcteFloat;
        }
        else if(lexeme.matches(regexCteBool)) {
            category = CategoryList.TcteBool;
        }
        else if(lexeme.matches(regexCteChar)) {
            category = CategoryList.TcteChar;
        }
        else if(lexeme.matches(regexCteString)) {
            category = CategoryList.TcteString;
        }
//        else if(lexeme.matches(regexVecInt)) {
//            category = CategoryList.TvecInt;
//        }
//        else if(lexeme.matches(regexVecFloat)) {
//            category = CategoryList.TvecFloat;
//        }
//        else if(lexeme.matches(regexVecBool)) {
//            category = CategoryList.TvecBool;
//        }
//        else if(lexeme.matches(regexVecChar)) {
//            category = CategoryList.TvecChar;
//        }
//        else if(lexeme.matches(regexVecString)) {
//            category = CategoryList.TvecStr;
//        }

        return category;
    }

}
