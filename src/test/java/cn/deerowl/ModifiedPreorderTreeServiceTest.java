package cn.deerowl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ModifiedPreorderTreeServiceTest {

    @Autowired
    private TreeService treeService;

    @Test
    public void testGetTree(){
        Category c = treeService.getTree(24);
        print(c, "");
    }

    private void print(Category c, String prefix){
        System.out.println(prefix + c.getId() + ". " +  c.getName());
        for (Category child : c.getSubCategories()){
            print(child, prefix+" ");
        }
    }

    @Test
    public void testAddCategories(){
        Category category = new Category("BOOK");
        Category subCategory = new Category("ADULT" );
        Category subCategory2 = new Category("CHILD");
        category.addSubCategory(subCategory);
        category.addSubCategory(subCategory2);
        treeService.addCategoryList(category);
    }

    @Test
    public void getAllRoots(){
        List<Category> categoryList = treeService.getRoots();
        for (Category c : categoryList){
            print(c, "");
        }
    }
}
