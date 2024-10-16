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
 * 
 * @author Danny South
 * @version October 13, 2024
 */
public class RedBlackTree<E> {
    /**
     * The head of the RBT.
     */
    Node root;
    /**
     * Number of elements.
     */
    int size;

    /**
     * A Node class for the RBT with key, value, left, right, color, and parent.
     * 
     */
    protected class Node {
        public String key;
        public E value;
        public Node left;
        public Node right;
        public Node parent;
        public boolean color; // true = red, false = black

        /**
         * Initialize the attributes of a Node.
         * 
         * @param key    string for placing the item properly
         * @param value  the data
         * @param parent parent node of this node; null for head
         * @param color  insertions are red
         */
        public Node(String key, E value, Node parent, boolean color) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.left = null;
            this.right = null;
            this.color = color;
        }

        /**
         * Gets the depth of the given node.
         * 
         * @return the number of levels up to root
         */
        public int getDepth() {
            Node par = this.parent;
            int count = 1;
            while (par != null) {
                par = par.parent;
                count++;
            }

            return count;
        }

        /**
         * Gets the black depth of the given node.
         * 
         * @return the number of black levels up to root
         */
        public int getBlackDepth() {
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

    /**
     * Initialize an empty RBT.
     * 
     */
    public RedBlackTree() {
        root = null;
        // Start with an empty tree. This is the one time we can have a null ptr instead
        // of a null key node
        size = 0;
    }

    /**
     * Insert a new node. Insertions are red.
     * 
     * @param key   key for this node
     * @param value data for this node
     */
    public void insert(String key, E value) {
        Node loc = find(key);
        if (loc == null) {// first insertion - root
            root = new Node(key, value, null, false);
            root.left = new Node(null, null, root, false);
            root.right = new Node(null, null, root, false);
            return;
        }

        if (loc.key != null) {// duplicate
            return;
        }

        loc.key = key;// insert as red with null node children
        loc.value = value;
        loc.color = true;
        loc.left = new Node(null, null, loc, false);
        loc.right = new Node(null, null, loc, false);
        size++;
        if (isRed(loc.parent)) {// fix to maintain RBT properties
            fix(loc, false);
        }
    }

    /**
     * Delete a node.
     * 
     * @param key of node to remove
     */
    public void delete(String key) {
        Node loc = find(key);
        if (loc == null || loc.key == null) {// not found
            return;
        }

        if (loc.left.key == null && loc.right.key == null) {// no children
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
        } else if (loc.left.key == null) {// right child only
            if (loc == root) {
                root = loc.right;
            } else {
                if (loc.parent.right == loc) {
                    loc.parent.right = loc.right;
                } else {
                    loc.parent.left = loc.right;
                }
                loc.right.parent = loc.parent;
            }
            fix(loc.right, true);
        } else if (loc.right.key == null) {// left child only
            if (loc == root) {
                root = loc.left;
            } else {
                if (loc.parent.right == loc) {
                    loc.parent.right = loc.left;
                } else {
                    loc.parent.left = loc.left;
                }
                loc.left.parent = loc.parent;
            }
            fix(loc.left, true);
        } else if (loc.left.key != null && loc.right.key != null) {// two children
            Node successor = loc.right;
            while (successor.left.key != null) {
                successor = successor.left;
            }
            E srValue = successor.value;
            String srKey = successor.key;
            delete(successor.key);
            loc.value = srValue;
            loc.key = srKey;
            fix(loc, true);
        }
        size--;
    }

    /**
     * Combined fix for insertions and deletions to maintain RBT properties.
     * 
     * @param node   node to begin fix from
     * @param delete fixing a deletion or not
     */
    private void fix(Node node, boolean delete) {
        Node par = node.parent;
        Node gp = node.parent.parent;
        if (par == root) {// end recursion
            return;
        }
        if (delete && node.left.key == null && node.right.key == null) {// flip color of childless node
            node.color = !node.color;
            return;
        }

        Node unc = null;
        boolean isNewNodeLeftChild = par.left == node;
        boolean isZigZag = false;
        if (gp != null) {
            if (gp.left == par) {// setting up proper rotation scheme
                isZigZag = !isNewNodeLeftChild;
                if (gp.right.key != null) {
                    unc = gp.right;
                }
            } else {
                isZigZag = isNewNodeLeftChild;
                if (gp.left.key != null) {
                    unc = gp.left;
                }
            }
            if (unc != null) {
                if (isBlack(unc)) {// black uncle
                    if (isNewNodeLeftChild) {
                        rotateRight(node, isZigZag);
                    } else {
                        rotateLeft(node, isZigZag);
                    }
                } else {// red uncle
                    if (gp == root) {
                        gp.color = false;
                    } else {
                        gp.color = !gp.color;
                    }
                    par.color = !par.color;
                    unc.color = !unc.color;
                    return;
                }
            } else {
                if (gp == root) {
                    gp.color = false;
                } else {
                    gp.color = !gp.color;
                }
                par.color = !par.color;
            }
        }

        fix(par, delete);
    }

    /**
     * Rotate left.
     * 
     * @param node     base of rotation
     * @param isZigZag whether rotation is first part of a two-part rotation
     */
    private void rotateLeft(Node node, boolean isZigZag) {
        Node par = node.parent;
        Node gp = par.parent;
        if (isZigZag) {// first part of a two-part rotation != the same rotation standing alone
            gp.left = node;
            par.right = node.left;
            node.left = par;
            rotateRight(par, false);
        } else {
            boolean gpIsRoot = false;
            if (gp.parent == null) {
                gpIsRoot = true;
            }
            Node alpha = par.left;
            Node ggp = null;
            if (gpIsRoot) {
                root = par;
            } else {
                ggp = gp.parent;
                if (ggp.left == gp) {
                    ggp.left = par;
                } else {
                    ggp.right = par;
                }
            }
            par.color = false;
            par.left = gp;
            gp.color = true;
            gp.right = alpha;
        }
    }

    /**
     * Rotate right.
     * 
     * @param node     base of rotation
     * @param isZigZag whether rotation is first part of a two-part rotation
     */
    private void rotateRight(Node node, boolean isZigZag) {
        Node par = node.parent;
        Node gp = par.parent;
        if (gp == null) {
            return;
        }
        if (isZigZag) {// two-part rotation
            gp.right = node;
            par.left = node.right;
            node.right = par;
            rotateLeft(par, false);
        } else {
            boolean gpIsRoot = false;
            if (gp == root) {
                gpIsRoot = true;
            }
            Node alpha = par.right;
            Node ggp = null;
            if (gpIsRoot) {
                root = par;
            } else {
                ggp = gp.parent;
                if (ggp.left == gp) {
                    ggp.left = par;
                } else {
                    ggp.right = par;
                }
            }
            par.color = false;
            par.right = gp;
            gp.color = true;
            gp.left = alpha;
        }
    }

    /**
     * Find a node by key.
     * 
     * @param key of node to find
     * @return node if found, null node if not, null if RBT is empty
     */
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

    /**
     * Returns data from a node.
     * 
     * @param key of node
     * @return the node's data
     */
    public E getValue(String key) {
        Node loc = find(key);
        if (loc == null || loc.key == null || loc.key != key) {
            return null;
        }

        return loc.value;
    }

    /**
     * Returns whether the RBT is empty or not.
     * 
     * @return true if empty, false if not
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Returns the depth of the node.
     * 
     * @param key of node
     * @return depth of node or 0
     */
    public int getDepth(String key) {
        Node node = find(key);
        if (node == null || node.key == null) {
            return 0;
        }
        return node.getDepth();
    }

    /**
     * Check the red color of a node.
     * 
     * @param node
     * @return red or not
     */
    private boolean isRed(Node node) {
        return node.key != null && node.color; // Red is true
    }

    /**
     * Check the black color of a node.
     * 
     * @param node
     * @return black or not
     */
    private boolean isBlack(Node node) {
        return node.key == null || !node.color; // Black is false, and null nodes are black
    }

    /**
     * Get size of RBT.
     * 
     * @return size
     */
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
