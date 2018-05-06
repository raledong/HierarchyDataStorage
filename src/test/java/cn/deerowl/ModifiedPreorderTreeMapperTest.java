package cn.deerowl;

import cn.deerowl.modified_preorder_tree.CategoryNode;
import cn.deerowl.modified_preorder_tree.CategoryNodeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ModifiedPreorderTreeMapperTest {

    @Autowired
    private CategoryNodeMapper mapper;

    @Test
    public void testGetAll(){
        mapper.getAll();
    }

    @Test
    public void testAdd(){
        mapper.addCategoryTo("testCategory", 2);
    }

    @Test
    public void testDel(){
        mapper.deleteCategory(1);
    }

    @Test
    public void testGetMaxRight(){
        mapper.getMaxRightValue();
    }
}
