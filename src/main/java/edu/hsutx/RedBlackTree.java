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
            fixDeletion(loc);

        } else if (loc.right.key == null) {
            if (loc == root) {
                root = loc.left;
            } else {
                loc.parent.left = loc.left;
            }
            fixDeletion(loc);

        } else if (loc.left.key != null && loc.right.key != null) {
            Node successor = loc.right;
            while (successor.left.key != null) {
                successor = successor.left;
            }
            E srValue = successor.value;
            String srKey = successor.key;
            delete(successor.key);
            loc.value = srValue;
            loc.key = srKey;
            fixDeletion(loc);

        }
        size--;
    }

    private void fixInsertion(Node node) {

        Node newpar = node.parent;
        Node gp = node.parent.parent;
        if (newpar == root) {

            return;
        }

        Node newunc = null;
        boolean isNewNodeLeftChild = newpar.left == node;
        boolean isZigZag = false;
        if (gp != null) {

            if (gp.left == newpar) {
                isZigZag = !isNewNodeLeftChild;
                if (gp.right.key != null) {
                    newunc = gp.right;
                }
            } else {
                isZigZag = isNewNodeLeftChild;
                if (gp.left.key != null) {
                    newunc = gp.left;
                }
            }

            if (newunc != null) {
                if (isRed(newunc)) {
                    if (gp == root) {
                        gp.color = false;
                    } else {
                        gp.color = !gp.color;
                    }
                    newpar.color = !newpar.color;
                    newunc.color = !newunc.color;
                    return;
                } else {

                    if (isNewNodeLeftChild) {
                        rotateRight(node, isZigZag);
                    } else {
                        rotateLeft(node, isZigZag);
                    }

                }
            } else {
                if (gp == root) {
                    gp.color = false;
                } else {
                    gp.color = !gp.color;
                }
                newpar.color = !newpar.color;
            }
        }

        fixInsertion(newpar);

    }

    private void fixDeletion(Node node) {
        fixInsertion(node);

    }

    private void rotateLeft(Node node, boolean isZigZag) {

        Node par = node.parent;
        Node gp = par.parent;
        if (isZigZag) {
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

    private void rotateRight(Node node, boolean isZigZag) {

        Node par = node.parent;
        Node gp = par.parent;
        if (gp == null) {
            return;
        }
        if (isZigZag) {
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
