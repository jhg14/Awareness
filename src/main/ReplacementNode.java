package main;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JHGWhite on 18/05/2016.
 */
public class ReplacementNode {

    private List<ReplacementNode> group;

    public ReplacementNode () {
        group = new ArrayList<>();
        group.add(this);

    }

    public void setGroup(List<ReplacementNode> newGroup) {
        this.group = newGroup;
    }

    public List<ReplacementNode> getGroup() {
        return group;
    }

    public List<ReplacementNode> mergeGroup (List<ReplacementNode> toMerge) {
        List<ReplacementNode> toReturn = new ArrayList<>();

        group.forEach((n) -> toReturn.add(n));
        toMerge.forEach((n) -> toReturn.add(n));

        return toReturn;
    }

}
