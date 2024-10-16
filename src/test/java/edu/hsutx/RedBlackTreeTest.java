package edu.hsutx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the RedBlackTree class.
 */
public class RedBlackTreeTest {

    private RedBlackTree<Integer> tree;

    @BeforeEach
    public void setUp() {
        tree = new RedBlackTree<>();
    }

    /* ------------------ Insertion Tests ------------------ */

    @Test
    public void testInsertIntoEmptyTree() {
        tree.insert("root", 1);
        assertFalse(tree.isEmpty(), "Tree should not be empty after insertion.");
        assertTrue(tree.validateRedBlackTree(), "Tree should be a valid Red-Black Tree after insertion.");
        assertEquals(1, tree.getValue("root"), "Inserted value should be retrievable.");
    }

    @Test
    public void testInsertLeftChild() {
        tree.insert("root", 1);
        tree.insert("left", 2);
        assertTrue(tree.validateRedBlackTree(), "Tree should be valid after inserting left child.");
        assertEquals(2, tree.getValue("left"), "Left child value should be retrievable.");
    }

    @Test
    public void testInsertRightChild() {
        tree.insert("root", 1);
        tree.insert("right", 3);
        assertTrue(tree.validateRedBlackTree(), "Tree should be valid after inserting right child.");
        assertEquals(3, tree.getValue("right"), "Right child value should be retrievable.");
    }

    @Test
    public void testInsertMultiple() {
        tree.insert("m", 10);
        tree.insert("c", 20);
        tree.insert("t", 30);
        tree.insert("a", 40);
        tree.insert("e", 50);
        tree.insert("s", 60);
        tree.insert("z", 70);

        assertTrue(tree.validateRedBlackTree(), "Tree should be valid after multiple insertions.");
        assertEquals(10, tree.getValue("m"));
        assertEquals(20, tree.getValue("c"));
        assertEquals(30, tree.getValue("t"));
        assertEquals(40, tree.getValue("a"));
        assertEquals(50, tree.getValue("e"));
        assertEquals(60, tree.getValue("s"));
        assertEquals(70, tree.getValue("z"));
    }

    @Test
    public void testInsertDuplicateKey() {
        tree.insert("dup", 100);
        tree.insert("dup", 200); // Assuming duplicates are not allowed

        assertTrue(tree.validateRedBlackTree(), "Tree should remain valid after attempting to insert duplicate key.");
        assertEquals(100, tree.getValue("dup"), "Value should remain unchanged when inserting duplicate key.");
    }

    @Test
    public void testInsertRequiresRecoloring() {
        // Insert nodes to create a situation that requires recoloring
        tree.insert("a", 1);
        tree.insert("b", 2);
        tree.insert("c", 3);

        assertTrue(tree.validateRedBlackTree(), "Tree should be valid after insertions requiring recoloring.");
        assertEquals(1, tree.getValue("a"));
        assertEquals(2, tree.getValue("b"));
        assertEquals(3, tree.getValue("c"));
    }

    @Test
    public void testInsertRequiresLeftRotation() {
        // Insert nodes in ascending order to cause left rotations
        tree.insert("a", 1);
        tree.insert("b", 2);
        tree.insert("c", 3);

        assertTrue(tree.validateRedBlackTree(), "Tree should be valid after insertions requiring left rotations.");
        assertEquals(1, tree.getValue("a"));
        assertEquals(2, tree.getValue("b"));
        assertEquals(3, tree.getValue("c"));
    }

    @Test
    public void testInsertRequiresRightRotation() {
        // Insert nodes in descending order to cause right rotations
        tree.insert("c", 3);
        tree.insert("b", 2);
        tree.insert("a", 1);

        assertTrue(tree.validateRedBlackTree(), "Tree should be valid after insertions requiring right rotations.");
        assertEquals(3, tree.getValue("c"));
        assertEquals(2, tree.getValue("b"));
        assertEquals(1, tree.getValue("a"));
    }

    /* ------------------ Deletion Tests ------------------ */

    @Test
    public void testDeleteLeafNode() {
        tree.insert("m", 10);
        tree.insert("c", 20);
        tree.insert("t", 30);
        tree.insert("a", 40); // Leaf
        tree.insert("e", 50);

        tree.delete("a"); // Deleting a leaf node

        assertNull(tree.getValue("a"), "Leaf node should be deleted.");
        assertTrue(tree.validateRedBlackTree(), "Tree should be valid after deleting a leaf node.");
    }

    @Test
    public void testDeleteNodeWithOneChild() {
        tree.insert("m", 10);
        tree.insert("c", 20);
        tree.insert("t", 30);
        tree.insert("a", 40);
        tree.insert("e", 50);
        tree.insert("d", 60); // 'c' has two children 'a' and 'e';'e' has one child 'd'

        // tree at this point is:
        // a, 40, false, left: null, right: null
        // c, 20, true, left: a, right: e
        // d, 60, true, left: null, right: null
        // e, 50, false, left: d, right: null
        // m, 10, false, left: c, right: t
        // t, 30, false, left: null, right: null

        // and can be seen with the tool at
        // https://www.cs.usfca.edu/~galles/visualization/RedBlack.html

        tree.delete("e"); // Deleting node 'e' which has one child 'd'

        assertNull(tree.getValue("e"), "Node with one child should be deleted.");
        assertTrue(tree.validateRedBlackTree(), "Tree should be valid after deleting a node with one child.");
    }

    @Test
    public void testDeleteNodeWithTwoChildren() {
        tree.insert("m", 10);
        tree.insert("c", 20);
        tree.insert("t", 30);
        tree.insert("a", 40);
        tree.insert("e", 50);
        tree.insert("s", 60);
        tree.insert("z", 70);

        tree.delete("c"); // 'c' has two children 'a' and 'e'

        assertNull(tree.getValue("c"), "Node with two children should be deleted.");
        assertTrue(tree.validateRedBlackTree(), "Tree should be valid after deleting a node with two children.");
        assertEquals(40, tree.getValue("a"), "Left child should still exist.");
        assertEquals(50, tree.getValue("e"), "Right child should still exist.");
    }

    @Test
    public void testDeleteRootNode() {
        tree.insert("m", 10);
        tree.delete("m");

        assertNull(tree.getValue("m"), "Root node should be deleted.");
        assertTrue(tree.isEmpty(), "Tree should be empty after deleting the root node.");
        assertTrue(tree.validateRedBlackTree(), "Tree should be valid after deleting the root node.");
    }

    @Test
    public void testDeleteRequiresRecoloring() {
        // Insert nodes to create a specific structure
        tree.insert("a", 1);
        tree.insert("c", 2);
        tree.insert("e", 3);
        tree.insert("g", 4);
        tree.insert("i", 5);
        tree.insert("k", 6);
        tree.insert("m", 7);

        // Delete a node that requires recoloring to maintain properties
        tree.delete("e");

        assertNull(tree.getValue("e"), "Node requiring recoloring should be deleted.");
        assertTrue(tree.validateRedBlackTree(), "Tree should be valid after deletion requiring recoloring.");
    }

    @Test
    public void testDeleteRequiresRotations() {
        // Insert nodes to create a situation that requires rotations during deletion
        tree.insert("m", 10);
        tree.insert("c", 20);
        tree.insert("t", 30);
        tree.insert("a", 40);
        tree.insert("e", 50);
        tree.insert("s", 60);
        tree.insert("z", 70);

        // Deleting a node that will cause rotations
        tree.delete("c");

        assertNull(tree.getValue("c"), "Node requiring rotations should be deleted.");
        assertTrue(tree.validateRedBlackTree(), "Tree should be valid after deletion requiring rotations.");
    }

    /* ------------------ Additional Tests ------------------ */

    @Test
    public void testIsEmptyOnNewTree() {
        assertTrue(tree.isEmpty(), "Newly created tree should be empty.");
    }

    @Test
    public void testIsEmptyAfterInsertions() {
        tree.insert("m", 10);
        assertFalse(tree.isEmpty(), "Tree should not be empty after insertions.");
    }

    @Test
    public void testIsEmptyAfterDeletions() {
        tree.insert("m", 10);
        tree.delete("m");
        assertTrue(tree.isEmpty(), "Tree should be empty after deleting all nodes.");
    }

    @Test
    public void testGetDepth() {
        tree.insert("m", 10);
        tree.insert("c", 20);
        tree.insert("t", 30);
        tree.insert("a", 40);
        tree.insert("e", 50);
        tree.insert("s", 60);
        tree.insert("z", 70);

        assertEquals(1, tree.getDepth("m"), "Depth of root should be 1.");
        assertEquals(2, tree.getDepth("c"), "Depth of child 'c' should be 2.");
        assertEquals(3, tree.getDepth("a"), "Depth of child 'a' should be 3.");
        assertEquals(0, tree.getDepth("nonexistent"), "Depth of nonexistent key should be 0.");
    }

    @Test
    public void testGetValue() {
        tree.insert("x", 100);
        tree.insert("y", 200);

        assertEquals(100, tree.getValue("x"), "Value of key 'x' should be 100.");
        assertEquals(200, tree.getValue("y"), "Value of key 'y' should be 200.");
        assertNull(tree.getValue("z"), "Value of nonexistent key should be null.");
    }

    @Test
    public void testFind() {
        tree.insert("apple", 1);
        tree.insert("banana", 2);
        tree.insert("cherry", 3);

        assertNotNull(tree.find("banana"), "Find should return a node for existing key.");
        assertNull(tree.find("date").key, "Find should return null for nonexistent key.");
        // slightly modified to test key since find returns null NODE
    }

    @Test
    public void testValidateRedBlackTreeOnEmptyTree() {
        assertTrue(tree.validateRedBlackTree(), "Empty tree should be a valid Red-Black Tree.");
    }

    @Test
    public void testValidateRedBlackTreeAfterInsertions() {
        tree.insert("m", 10);
        tree.insert("c", 20);
        tree.insert("t", 30);
        tree.insert("a", 40);
        tree.insert("e", 50);
        tree.insert("s", 60);
        tree.insert("z", 70);

        assertTrue(tree.validateRedBlackTree(), "Tree should be valid after multiple insertions.");
    }

    @Test
    public void testValidateRedBlackTreeAfterDeletions() {
        tree.insert("m", 10);
        tree.insert("c", 20);
        tree.insert("t", 30);
        tree.insert("a", 40);
        tree.insert("e", 50);
        tree.insert("s", 60);
        tree.insert("z", 70);

        tree.delete("a");
        tree.delete("t");

        assertTrue(tree.validateRedBlackTree(), "Tree should be valid after deletions.");
    }
}
