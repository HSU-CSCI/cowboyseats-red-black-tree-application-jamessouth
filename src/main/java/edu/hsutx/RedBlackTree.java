package edu.hsutx;

/**
 * @author Todd Dole
 * @version 1.0
 * Starting Code for the CSCI-3323 Red-Black Tree assignment
 * Students must complete the todos and get the tests to pass
 */

/**
 * A Red-Black Tree that takes int key and String value for each node.
 * Follows the properties of a Red-Black Tree:
 * 1. Every node is either red or black.
 * 2. The root is always black.
 * 3. Every leaf (NIL node) is black.
 * 4. If a node is red, then both its children are black.
 * 5. For each node, all simple paths from the node to descendant leaves have
 * the same number of black nodes.
 */
public class RedBlackTree<E> {
    Node root;
    int size;

    protected class Node {
        public String key;
        public E value;
        public Node left;
        public Node right;
        public Node parent;
        public boolean color; // true = red, false = black

        public Node(String key, E value, Node parent, boolean color) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.left = null;
            this.right = null;
            this.color = color;
        }

        // TODO - add comments as appropriate including a javadoc for each method
        public int getDepth() {

            Node par = this.parent;
            int count = 1;
            while (par != null) {
                par = par.parent;
                count++;
            }

            return count;

        }

        public int getBlackDepth() {
            // todo - calculate the depth of the node counting only black nodes and return
            // an int value
            Node par = this.parent;
            int count = 1;
            while (par != null) {
                par = par.parent;
                if (!par.color) {
                    count++;
                }
            }

            return count;
        }
    }

    public RedBlackTree() {
        root = null; // Start with an empty tree. This is the one time we can have a null ptr instead
                     // of a null key node
        size = 0;
    }

    public void insert(String key, E value) {

        Node loc = find(key);
        if (loc == null) {
            root = new Node(key, value, null, false);
            root.left = new Node(null, null, root, false);
            root.right = new Node(null, null, root, false);
            return;
        }

        if (loc.key != null) {
            return;
        }
        loc.key = key;
        loc.value = value;
        loc.color = true;
        loc.left = new Node(null, null, loc, false);
        loc.right = new Node(null, null, loc, false);
        size++;
        if (isRed(loc.parent)) {
            fixInsertion(loc);
        }
    }

    public void delete(String key) {
        // Will need to handle three cases similar to the Binary Search Tree
        // 1. Node to be deleted has no children
        // 2. Node to be deleted has one child
        // 3. Node to be deleted has two children
        // Additionally, you must handle rebalancing after deletion to restore Red-Black
        // Tree properties
        // make sure to subtract one from size if node is successfully removed

        Node loc = find(key);
        if (loc == null || loc.key == null) {
            return;
        }

        if (loc.left.key == null && loc.right.key == null) {
            if (loc == root) {
                root = null;
                size = 0;
                return;
            }
            loc.left = null;
            loc.right = null;
            loc.key = null;
            loc.value = null;
            loc.color = false;
        } else if (loc.left.key == null) {
            if (loc == root) {
                root = loc.right;
            } else {
                loc.parent.right = loc.right;
            }
        } else if (loc.right.key == null) {
            if (loc == root) {
                root = loc.left;
            } else {
                loc.parent.left = loc.left;
            }
        } else if (loc.left.key != null && loc.right.key != null) {
            Node successor = loc.right;
            while (successor.left != null) {
                successor = successor.left;
            }
            E srValue = successor.value;
            String srKey = successor.key;
            delete(successor.key);
            loc.value = srValue;
            loc.key = srKey;
        }
        size--;
        fixDeletion(loc);
    }

    private void fixInsertion(Node node) {
        // TODO - Implement the fix-up procedure after insertion
        // Ensure that Red-Black Tree properties are maintained (recoloring and
        // rotations).
        // Hint: You will need to deal with red-red parent-child conflicts

        Node gp = node.parent.parent;
        Node lc = gp.left;
        Node rc = gp.right;

        if (isRed(lc) && isRed(rc)) {
            gp.color = !gp.color;
            lc.color = !lc.color;
            rc.color = !rc.color;
        }

    }

    private void fixDeletion(Node node) {
        // TODO - Implement the fix-up procedure after deletion
        // Ensure that Red-Black Tree properties are maintained (recoloring and
        // rotations).
    }

    private void rotateLeft(Node node) {
        // TODO - Implement left rotation
        // Left rotation is used to restore balance after insertion or deletion
    }

    private void rotateRight(Node node) {
        // TODO - Implement right rotation
        // Right rotation is used to restore balance after insertion or deletion
    }

    Node find(String key) {

        if (isEmpty()) {
            return null;
        }

        Node node = root;
        while (key != node.key) {
            if (key.compareToIgnoreCase(node.key) < 0) {
                if (node.left.key == null) {
                    return node.left;
                }
                node = node.left;
            } else {
                if (node.right.key == null) {
                    return node.right;
                }
                node = node.right;
            }
        }

        return node;

    }

    public E getValue(String key) {

        Node loc = find(key);
        if (loc == null || loc.key == null || loc.key != key) {
            return null;
        }

        return loc.value;

    }

    public boolean isEmpty() {
        return root == null;
    }

    // returns the depth of the node with key, or 0 if it doesn't exist
    public int getDepth(String key) {
        Node node = find(key);
        if (node == null || node.key == null) {
            return 0;
        }
        return node.getDepth();
    }

    // Helper methods to check the color of a node
    private boolean isRed(Node node) {
        return node.key != null && node.color; // Red is true
    }

    private boolean isBlack(Node node) {
        return node.key == null || !node.color; // Black is false, and null nodes are black
    }

    public int getSize() {
        return size;
    }

    // Do not alter this method
    public boolean validateRedBlackTree() {
        // Rule 2: Root must be black
        if (root == null) {
            return true; // An empty tree is trivially a valid Red-Black Tree
        }
        if (isRed(root)) {
            return false; // Root must be black
        }

        // Start recursive check from the root
        return validateNode(root, 0, -1);
    }

    // Do not alter this method
    // Helper method to check if the current node maintains Red-Black properties
    private boolean validateNode(Node node, int blackCount, int expectedBlackCount) {
        // Rule 3: Null nodes (leaves) are black
        if (node.key == null) {
            if (expectedBlackCount == -1) {
                expectedBlackCount = blackCount; // Set the black count for the first path
            }
            return blackCount == expectedBlackCount; // Ensure every path has the same black count
        }

        // Rule 1: Node is either red or black (implicit since we use a boolean color
        // field)

        // Rule 4: If a node is red, its children must be black
        if (isRed(node)) {
            if (isRed(node.left) || isRed(node.right)) {
                return false; // Red node cannot have red children
            }
        } else {
            blackCount++; // Increment black node count on this path
        }

        // Recurse on left and right subtrees, ensuring they maintain the Red-Black
        // properties
        return validateNode(node.left, blackCount, expectedBlackCount) &&
                validateNode(node.right, blackCount, expectedBlackCount);
    }
}
