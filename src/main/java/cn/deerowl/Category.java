package cn.deerowl;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Category {
    
    private int id;
    private String name;
    private List<Category> subCategories;

    public Category(int id, String name){
        this(name);
        this.id = id;
    }

    public Category(String name){
        this.name = name;
        subCategories = new ArrayList<Category>();
    }
    public void addSubCategory(Category subCategory){
        subCategories.add(subCategory);
    }

    public boolean isLeaf(){
        return subCategories==null || subCategories.size() == 0;
    }
}
