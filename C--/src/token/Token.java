package token;

import categories.CategoryList;
import categories.LexemeCategoryMap;

public class Token {

    private CategoryList category;
    private int lineNumber, columnNumber;
    private String id;

    public Token(String lexeme, int lineNumber, int columnNumber) {
        this.id = lexeme;
        CategoryList cat = LexemeCategoryMap.MapLexemeCategory.get(lexeme);
        if(cat == null) {
            category = getCategoryFromRegex(lexeme);
        } else {
            category = cat;
        }
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber - lexeme.length();
    }

    @Override
    public String toString() {
        String format = "              [%04d, %04d] (%04d, %20s) {%s}";
        return String.format(format, lineNumber, columnNumber, category.ordinal(), category.toString(), id);
    }

    public CategoryList getCategoryFromRegex(String lexeme) {
        this.category = CategoryList.Tunknown;

        String regexId = "([a-z])([a-zA-Z]|[0-9]|_)*";
        String regexFuncId = "([A-Z])([a-zA-Z]|[0-9]|_)*";
        String regexCteInt = "[0-9]+";
        String regexCteFloat = "[0-9]+(\\.[0-9]+)?";
        String regexCteBool = "(true|false)";
        String regexCteChar = "'(([a-z]|[A-Z]|[0-9]|[ /\\\\!@#$%&*()_\\-=+\\[\\]{}><?.:;,\"])|(\\\\'))'";
        String regexCteString = "\"([a-z]|[A-Z]|[0-9]|[ /\\\\!@#$%&*()_\\-=+\\[\\]{}><?.;:,\"'])*\"";

        if(lexeme.matches(regexId)) {
            this.category = CategoryList.TnameId;
        }
        else if(lexeme.matches(regexFuncId)) {
            this.category = CategoryList.TfuncId;
        }
        else if(lexeme.matches(regexCteInt)) {
            this.category = CategoryList.TcteInt;
        }
        else if(lexeme.matches(regexCteFloat)) {
            this.category = CategoryList.TcteFloat;
        }
        else if(lexeme.matches(regexCteBool)) {
            this.category = CategoryList.TcteBool;
        }
        else if(lexeme.matches(regexCteChar)) {
            this.category = CategoryList.TcteChar;
        }
        else if(lexeme.matches(regexCteString)) {
            this.category = CategoryList.TcteString;
        }


        return this.category;
    }

    public void setTokenCategory(CategoryList tokenCategory) {
        this.category = tokenCategory;
    }

    public CategoryList getTokenCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }
}
