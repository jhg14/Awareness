package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by JHGWhite on 18/05/2016.
 */
public class ReplacementNode {

    private List<ReplacementNode> group;

    private int x;
    private int y;

    //private char currentLetter;

    public ReplacementNode (int x, int y) {
        group = new ArrayList<>();
        group.add(this);
        this.x = x;
        this.y = y;
    }

    public int getX() {return x;}
    public int getY() {return y;}

//    public void setLetter(char c) {
//        currentLetter = c;
//    }
//
//    public char getLetter() {
//        return currentLetter;
//    }

    public void setGroup(List<ReplacementNode> newGroup) {
        this.group = newGroup;
    }

    public List<ReplacementNode> getGroup() {
        return group;
    }

    public void mergeAndSetGroup (ReplacementNode toMerge) {
        List<ReplacementNode> mergedGroup = new ArrayList<>();

        group.forEach((n) -> mergedGroup.add(n));
        toMerge.getGroup().forEach((n) -> mergedGroup.add(n));

        Set<ReplacementNode> temp = new HashSet<>();
        temp.addAll(mergedGroup);
        mergedGroup.clear();
        mergedGroup.addAll(temp);

        this.setGroup(mergedGroup);
        toMerge.setGroup(mergedGroup);
    }

}
