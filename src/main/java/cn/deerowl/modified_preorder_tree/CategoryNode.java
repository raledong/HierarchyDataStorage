package cn.deerowl.modified_preorder_tree;

import lombok.Data;

@Data
public class CategoryNode {

    private int id;
    private String name;
    private int lft;
    private int rgt;

    public boolean isLeaf(){
        return lft + 1 == rgt;
    }
}
