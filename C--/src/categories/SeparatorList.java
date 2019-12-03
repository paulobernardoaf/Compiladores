package categories;

import java.util.ArrayList;
import java.util.Arrays;

public class SeparatorList {

    private ArrayList<Character> separatorList = new ArrayList<>();
    private ArrayList<Character> separatorButTokenList = new ArrayList<>();

    public SeparatorList() {

        Character[] str1 = { ';', ' ', '(', ')', '\n', '\t', '{', '}',
                        '+', '-', '/', '*', '%', ':', '=', '^',
                        '<', '>', '|', '&', ',', '[', ']'};

        Character[] str2 = { ';', '(', ')', '{', '}',
                '+', '-', '/', '*', '%', '=',
                '<', '>', '&', ',', '[', ']'};

        separatorList.addAll(Arrays.asList(str1));
        separatorButTokenList.addAll(Arrays.asList(str2));


    }

    public ArrayList<Character> getSeparatorList() {
        return separatorList;
    }

    public ArrayList<Character> getSeparatorButTokenList() {
        return separatorButTokenList;
    }
}
