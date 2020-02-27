package categories;

import java.util.ArrayList;
import java.util.Arrays;

public class SeparatorList {

    private ArrayList<Character> separatorList = new ArrayList<>();
    private ArrayList<Character> separatorButTokenList = new ArrayList<>();
    private ArrayList<Character> separatorButComparator = new ArrayList<>();

    public SeparatorList() {

        Character[] str1 = { ';', ' ', '(', ')', '\n', '\t', '{', '}',
                        '+', '-', '/', '*', ':', '=', '^',
                        '<', '>', '|', '&', ',', '[', ']', '!', '\r'};

        Character[] str2 = { ';', '(', ')', '{', '}',
                '+', '-', '/', '*', '=',
                '<', '>', '&', ',', '[', ']', '!'};

        Character[] str3 = { '<', '>', '!', '='};

        separatorList.addAll(Arrays.asList(str1));
        separatorButTokenList.addAll(Arrays.asList(str2));
        separatorButComparator.addAll(Arrays.asList(str3));


    }

    public ArrayList<Character> getSeparatorList() {
        return separatorList;
    }

    public ArrayList<Character> getSeparatorButTokenList() {
        return separatorButTokenList;
    }

    public ArrayList<Character> getSeparatorButComparator() {
        return separatorButComparator;
    }
}
