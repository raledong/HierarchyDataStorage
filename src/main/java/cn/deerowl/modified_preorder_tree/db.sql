#建表语句
CREATE TABLE nested_category (
  category_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(20) NOT NULL,
  lft INT NOT NULL,
  rgt INT NOT NULL
);

# 插入第一组分类
INSERT INTO nested_category VALUES(1,'ELECTRONICS',1,20),(2,'TELEVISIONS',2,9),(3,'TUBE',3,4),
  (4,'LCD',5,6),(5,'PLASMA',7,8),(6,'PORTABLE ELECTRONICS',10,19),(7,'MP3 PLAYERS',11,14),(8,'FLASH',12,13),
  (9,'CD PLAYERS',15,16),(10,'2 WAY RADIOS',17,18);

# 插入第二组分类
INSERT INTO nested_category VALUES(11, 'BOOKS',19, 28),(12, 'CHILDREN', 20, 25),(13, 'PEGGY',21,22),(14, 'PRINCESS CHARM',23,24)
  ,(15,'ADULT',26,27);
SELECT * FROM nested_category ORDER BY category_id;


#获取全部根节点
SELECT * FROM nested_category c1 WHERE c1.category_id NOT IN (
  SELECT DISTINCT c2.category_id FROM nested_category c2, nested_category c3
  WHERE c2.lft > c3.lft AND c2.rgt < c3.rgt
);

#获取单个节点的全部子节点
SELECT * FROM nested_category c1, nested_category c2
WHERE c1.lft > c2.lft AND c1.rgt < c2.rgt
      AND c2.category_id = 2;

#获取单个节点的全部父节点
SELECT c1.* FROM nested_category c1, nested_category c2
WHERE c2.category_id = 12
      AND c1.lft < c2.lft
      AND c1.rgt > c2.rgt;

#获取单个节点的高度，即所有父节点的个数
SELECT c2.*, count(*) FROM nested_category c1, nested_category c2
WHERE c2.category_id = 12
      AND c1.lft < c2.lft
      AND c1.rgt > c2.rgt;

#获取单个节点的直接子节点
set @@sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
SELECT c1.* , (count(c2.category_id) - parentDepth.depth) as depth FROM
  nested_category c1,
  nested_category c2,
  (
    SELECT c2.*, count(*) as depth FROM nested_category c1, nested_category c2
    WHERE c2.category_id = 2
          AND c1.lft < c2.lft
          AND c1.rgt > c2.rgt
  )AS parentDepth
WHERE c1.lft>parentDepth.lft AND c1.rgt<parentDepth.rgt
      AND c2.lft <= parentDepth.lft AND c2.rgt>=parentDepth.rgt
GROUP BY c1.category_id
HAVING depth = 1;

#获取父节点
SELECT c1.* FROM nested_category c1, nested_category c2
WHERE c2.category_id = 9
      AND c1.lft < c2.lft
      AND c1.rgt > c2.rgt
ORDER BY c1.lft DESC
LIMIT 1;

#插入节点-只能作为当前节点的一个新节点
CREATE PROCEDURE addCategory(
  IN categoryName VARCHAR(255),
  IN parentId INT,
  OUT categoryID INT
)
BEGIN
  SELECT @right := rgt FROM nested_category c WHERE c.category_id = parentId;
  UPDATE nested_category SET rgt = rgt + 2 WHERE rgt >= @right;
  UPDATE nested_category SET lft = lft + 2 WHERE lft >= @right;
  INSERT INTO nested_category(name, lft, rgt) VALUES(categoryName, @right, @right+1);
  SELECT LAST_INSERT_ID() INTO categoryID;
END;

CALL addCategory('GAME',1, @categoryId);
SELECT @categoryId;
#删除该procedure
#DROP PROCEDURE addCategory

#删除节点
CREATE PROCEDURE delCategory(
  IN categoryID INT
)
BEGIN
  SELECT @myLeft := lft, @myRight := rgt, @myWidth := rgt - lft + 1
  FROM nested_category
  WHERE category_id = categoryID;

  DELETE FROM nested_category WHERE lft BETWEEN @myLeft AND @myRight;

  UPDATE nested_category SET rgt = rgt - @myWidth WHERE rgt > @myRight;
  UPDATE nested_category SET lft = lft - @myWidth WHERE lft > @myRight;
END;

CALL delCategory(1);

SELECT MAX(rgt) FROM nested_category;