package cn.deerowl.modified_preorder_tree;

import cn.deerowl.Category;
import cn.deerowl.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModifiedPreorderTreeService implements TreeService {
    @Autowired
    private CategoryNodeMapper mapper;

    @Override
    public Category getTree(int rootId) {
        List<CategoryNode> categoryNodes = mapper.getSubCategoryNodesIncludingSelf(rootId);
        if (categoryNodes==null || categoryNodes.size() ==0) return null;
        CategoryNode root = categoryNodes.remove(0);
        return getTree(root, categoryNodes);
    }

    private Category getTree(CategoryNode parentCategory, List<CategoryNode> nodes){
        Category category = new Category(parentCategory.getId(), parentCategory.getName());
        if (!parentCategory.isLeaf()){
            while (nodes.size() > 0){
                CategoryNode tmpNode = nodes.get(0);
                if (tmpNode.getLft() > parentCategory.getRgt()) break;
                nodes.remove(0);
                category.addSubCategory(getTree(tmpNode, nodes));
            }
        }
        return category;
    }

    @Override
    public List<Category> getRoots() {
        List<CategoryNode> roots = mapper.getRoots();
        List<Category> result = new ArrayList<Category>();
        for (CategoryNode n : roots){
            Category root = this.getTree(n.getId());
            result.add(root);
        }
        return result;
    }

    @Transactional
    @Override
    public int addCategory(String nodeName, int parentId) {
        return mapper.addCategoryTo(nodeName, parentId);
    }

    @Override
    @Transactional
    public void deleteCategory(int id) {
        mapper.deleteCategory(id);
    }

    /**
     * 这里都不考虑异常情况
     * @param category
     * @return
     */
    @Override
    public int addCategoryList(Category category) {
        int lftValue = mapper.getMaxRightValue() + 1;
        List<CategoryNode> nodes = new ArrayList<CategoryNode>();
        CategoryNode root = labelCategory(category, lftValue, nodes);
        mapper.addCategories(nodes);
        return root.getId();
    }

    /**
     * 传入lftValue并设置各个node的左右值
     * @param category
     * @param lftValue
     * @return rgtValue
     */
    private CategoryNode labelCategory(Category category, int lftValue, List<CategoryNode> nodes){
        CategoryNode categoryNode = new CategoryNode();
        nodes.add(categoryNode);
        categoryNode.setName(category.getName());
        categoryNode.setLft(lftValue);
        int rgtValue = lftValue + 1;
        if (category.isLeaf()){
            categoryNode.setRgt(rgtValue);
        }else{
            for (Category subCategory : category.getSubCategories()){
                rgtValue = labelCategory(subCategory, rgtValue, nodes).getRgt() + 1;
            }
            categoryNode.setRgt(rgtValue);
        }
        return categoryNode;
    }
}
