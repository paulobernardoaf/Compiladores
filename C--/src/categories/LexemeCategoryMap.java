package categories;

import java.util.HashMap;
import java.util.Map;

public class LexemeCategoryMap {

    public static Map<String, CategoryList> MapLexemeCategory = new HashMap<>();

    static  {
        MapLexemeCategory.put("Main", CategoryList.Tmain);
        MapLexemeCategory.put("global", CategoryList.Tglobal);
        MapLexemeCategory.put("int", CategoryList.Tint);
        MapLexemeCategory.put("float", CategoryList.Tfloat);
        MapLexemeCategory.put("string", CategoryList.Tstring);
        MapLexemeCategory.put("char", CategoryList.Tchar);
        MapLexemeCategory.put("bool", CategoryList.Tbool);
        MapLexemeCategory.put("void", CategoryList.Tvoid);
        MapLexemeCategory.put("if", CategoryList.Tif);
        MapLexemeCategory.put("else", CategoryList.Telse);
        MapLexemeCategory.put("during", CategoryList.Tduring);
        MapLexemeCategory.put("from", CategoryList.Tfrom);
        MapLexemeCategory.put("increment", CategoryList.Tincrement);
        MapLexemeCategory.put("to", CategoryList.Tto);
        MapLexemeCategory.put(",", CategoryList.Tcomma);
        MapLexemeCategory.put(";", CategoryList.TsemiCol);
        MapLexemeCategory.put("(", CategoryList.TbegBrac);
        MapLexemeCategory.put(")", CategoryList.TendBrac);
        MapLexemeCategory.put("[", CategoryList.TbegSqBrac);
        MapLexemeCategory.put("]", CategoryList.TendSqBrac);
        MapLexemeCategory.put("{", CategoryList.TbegCrBrac);
        MapLexemeCategory.put("}", CategoryList.TendCrBrac);
        MapLexemeCategory.put("print", CategoryList.Tprint);
        MapLexemeCategory.put("get", CategoryList.Tget);
        MapLexemeCategory.put("return", CategoryList.Treturn);
        MapLexemeCategory.put("&", CategoryList.TopConc);
        MapLexemeCategory.put("and", CategoryList.TopAnd);
        MapLexemeCategory.put("or", CategoryList.TopOr);
        MapLexemeCategory.put("not", CategoryList.TopNot);
        MapLexemeCategory.put("=", CategoryList.TopAtr);
        MapLexemeCategory.put("==", CategoryList.TopEq);
        MapLexemeCategory.put("!=", CategoryList.TopDif);
        MapLexemeCategory.put("+", CategoryList.TopAdd);
        MapLexemeCategory.put("-", CategoryList.TopSub);
        MapLexemeCategory.put("/", CategoryList.TopDiv);
        MapLexemeCategory.put("*", CategoryList.TopMult);
        MapLexemeCategory.put("%", CategoryList.TopMod);
        MapLexemeCategory.put("<", CategoryList.TopLowThen);
        MapLexemeCategory.put(">", CategoryList.TopGreThen);
        MapLexemeCategory.put("<=", CategoryList.TopLowThnE);
        MapLexemeCategory.put(">=", CategoryList.TopGreThnE);
    }

}
