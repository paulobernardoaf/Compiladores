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
            if(tokenCategory == null) {
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

        if(lexeme.matches("([a-z])([a-zA-Z]|[0-9]|_)*")) {
            category = CategoryList.TnameId;
        }
        else if(lexeme.matches("([A-Z])([a-zA-Z]|[0-9]|_)*")) {
            category = CategoryList.TfuncId;
        }
        else if(lexeme.matches("-?[0-9]+")) {
            category = CategoryList.TcteInt;
        }
        else if(lexeme.matches("-?[0-9]+(\\.[0-9]+)?")) {
            category = CategoryList.TcteFloat;
        }
        else if(lexeme.matches("(true|false)")) {
            category = CategoryList.TcteBool;
        }
        else if(lexeme.matches("'([a-z]|[A-Z]|[0-9]|[ /\\\\!@#$%&*()_\\-=+\\[\\]{}><?.;,\"'])'")) {
            category = CategoryList.TcteChar;
        }
        else if(lexeme.matches("\"([a-z]|[A-Z]|[0-9]|[ /\\\\!@#$%&*()_\\-=+\\[\\]{}><?.;,\"'])*\"")) {
            category = CategoryList.TcteString;
        }

        return category;
    }

}
