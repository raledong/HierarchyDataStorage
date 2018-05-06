package cn.deerowl;

import java.util.List;

public interface TreeService {

    /**
     * 获得rootId节点下的所有子节点
     * @param rootId
     * @return
     */
    Category getTree(int rootId);

    /**
     * 获得所有根节点
     * @return
     */
    List<Category> getRoots();


    /**
     * 添加一个分类
     * @param nodeName
     * @param parentId
     * @return
     */
    int addCategory(String nodeName, int parentId);

    /**
     * 删除一个分类
     * @param id
     * @return
     */
    void deleteCategory(int id);

    /**
     * 添加一个分类列表作为一个全新的分类
     * @param category
     * @return
     */
    int addCategoryList(Category category);
}
