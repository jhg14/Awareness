package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by JHGWhite on 18/05/2016.
 */

/*
 * This class is to represents tiles that have been
 * identified as being part of an enclosed space.
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

    public void setGroup(List<ReplacementNode> newGroup) {
        this.group = newGroup;
    }

    public List<ReplacementNode> getGroup() {
        return group;
    }


    /*
     * This method is used to merge 2 nodes' groups together.
     * This will be iterated until all nodes of a group share the
     * same information.
     */
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
        mergedGroup.sort((a, b) -> (b.hashCode()) - (a.hashCode()));
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
