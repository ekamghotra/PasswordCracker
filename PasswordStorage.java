import java.util.NoSuchElementException;

public class PasswordStorage {
  protected PasswordNode root; // the root of this BST that contains passwords
  private int size; // how many passwords are in the BST
  private final Attribute COMPARISON_CRITERIA; // what password information to use to determine
                                               // order in the tree

  /**
   * Constructor that creates an empty BST and sets the comparison criteria.
   * 
   * @param comparisonCriteria, the Attribute that will be used to determine order in the tree
   */
  public PasswordStorage(Attribute comparisonCriteria) {
    this.size = 0;
    this.COMPARISON_CRITERIA = comparisonCriteria;
  }

  /**
   * Getter for this BST's criteria for determining order in the three
   * 
   * @return the Attribute that is being used to make comparisons in the tree
   */
  public Attribute getComparisonCriteria() {
    return this.COMPARISON_CRITERIA;
  }

  /**
   * Getter for this BST's size.
   * 
   * @return the size of this tree
   */
  public int size() {
    return this.size;
  }

  /**
   * Determines whether or not this tree is empty.
   * 
   * @return true if it is empty, false otherwise
   */
  public boolean isEmpty() {
    return this.root == null;
  }

  /**
   * Provides in-order String representation of this BST, with each Password on its own line. The
   * String representation ends with a newline character ('\n')
   * 
   * @return this BST as a string
   */
  @Override
  public String toString() {
    return toStringHelper(this.root);
  }

  /**
   * Recursive method the uses an in-order traversal to create the string representation of this
   * tree.
   * 
   * @param currentNode, the root of the current tree
   * @return the in-order String representation of the tree rooted at current node
   */
  private String toStringHelper(PasswordNode currentNode) {
    if (currentNode == null) {
      return "";
    }

    String out = "";

    out += toStringHelper(currentNode.getLeft());
    out += currentNode.getPassword() + "\n";
    out += toStringHelper(currentNode.getRight());

    return out;
  }

  /**
   * Determines whether or not this tree is actually a valid BST.
   * 
   * @return true if it is a BST, false otherwise
   */
  public boolean isValidBST() {
    return isValidBSTHelper(this.root, Password.getMinPassword(), Password.getMaxPassword());
  }

  /**
   * Recurisvely determines if the the tree rooted at the current node is a valid BST. That is,
   * every value to the left of currentNode is "less than" the value in currentNode and every value
   * to the right of it is "greater than" it.
   *
   * @param currentNode, the root node of the current tree
   * @param lowerBound,  the smallest possible password
   * @param upperBound,  the largest possible password
   * @return true if the tree rooted at currentNode is a BST, false otherwise
   */
  private boolean isValidBSTHelper(PasswordNode currentNode, Password lowerBound,
      Password upperBound) {
    // MUST BE IMPLEMENTED RECURSIVELY
    // BASE CASE 1: the tree rooted at currentNode is empty, which does not violate any BST rules
    if (currentNode == null) {
      return true;
    }
    // BASE CASE 2: the current Password is outside of the upper OR lower bound for this subtree,
    // which is against
    // the rules for a valid BST. If we do not have a base case situation, we must use recursion to
    // verify
    // currentNode's child subtrees
    if (currentNode.getPassword().compareTo(lowerBound, COMPARISON_CRITERIA) <= 0
        || currentNode.getPassword().compareTo(upperBound, COMPARISON_CRITERIA) >= 0) {
      return false;
    }
    // RECURSIVE CASE 1: Check that the left subtree contains only Passwords greater than lowerBound
    // and less than
    // currentNode's Password; return false if this property is NOT satisfied
    Password currentPassword = currentNode.getPassword();
    boolean leftIsValid = isValidBSTHelper(currentNode.getLeft(), lowerBound, currentPassword);
    if (!leftIsValid) {
      return false;
    }
    // RECURSIVE CASE 2: Check that the right subtree contains only Passwords greater than
    // currentNode's Password
    // and less than upperBound; return false if this property is NOT satisfied
    boolean rightIsValid = isValidBSTHelper(currentNode.getRight(), currentPassword, upperBound);
    if (!rightIsValid) {
      return false;
    }
    // COMBINE RECURSIVE CASE ANSWERS: this is a valid BST if and only if both case 1 and 2 are
    // valid
    return false; // TODO
  }

  /**
   * Returns the password that matches the criteria of the provided key. Ex. if the COMPARISON
   * CRITERIA is OCCURRENCE and the key has an occurrence of 10 it will return the password stored
   * in the tree with occurrence of 10
   * 
   * @param key, the password that contains the information for the password we are searching for
   * @return the Password that matches the search criteria, if it does not exist in the tree it this
   *         will be null
   */
  public Password lookup(Password key) {
    return lookupHelper(key, root);
  }

  /**
   * Recursive helper method to find the matching password in this BST
   * 
   * @param key,         password containing the information we are searching for
   * @param currentNode, the node that is the current root of the tree
   * @return the Password that matches the search criteria, if it does not exist in the tree it this
   *         will be null
   */
  private Password lookupHelper(Password key, PasswordNode currentNode) {
    // THIS MUST BE IMPLEMENTED RECURSIVELY

    if (currentNode == null) {
      return null;
    }

    int comparison = key.compareTo(currentNode.getPassword(), this.COMPARISON_CRITERIA);

    if (comparison < 0) {
      return lookupHelper(key, currentNode.getLeft());
    } else if (comparison > 0) {
      return lookupHelper(key, currentNode.getRight());
    } else {
      return currentNode.getPassword();
    }
  }

  /**
   * Returns the best (max) password in this BST
   *
   * @return the best password in this BST
   * @throws NoSuchElementException if the BST is empty
   */
  public Password getBestPassword() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }

    PasswordNode current = root;

    while (current.getRight() != null) {
      current = current.getRight();
    }

    return current.getPassword();
  }

  /**
   * Returns the worst password in this BST
   *
   * @return the worst password in this BST
   * @throws NoSuchElementException if the BST is empty
   */
  public Password getWorstPassword() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }

    PasswordNode current = root;

    while (current.getLeft() != null) {
      current = current.getLeft(); // move to the leftmost node
    }

    return current.getPassword();
  }

  /**
   * Adds the Password to this BST.
   * 
   * @param toAdd, the password to be added to the tree
   * @throws IllegalArgumentException if the (matching) password object is already in the tree
   */
  public void addPassword(Password toAdd) {
    if (lookup(toAdd) != null) throw new IllegalArgumentException("Password is already in tree");

    if (isEmpty()) {
      root = new PasswordNode(toAdd);
      size++;
      } 
    else addPasswordHelper(toAdd, root);
  }

  /**
   * Recursive helper that traverses the tree and adds the password where it belongs
   * 
   * @param toAdd,       the password to add to the tree
   * @param currentNode, the node that is the current root of the (sub)tree
   * 
   * @return true if it was successfully added, false otherwise
   */
  private boolean addPasswordHelper(Password toAdd, PasswordNode currentNode) {
    // THIS MUST BE IMPLEMENTED RECURSIVELY
    PasswordNode newNode = new PasswordNode(toAdd);

    int result = newNode.getPassword().compareTo(currentNode.getPassword(), getComparisonCriteria());

    if (result < 0 && !currentNode.hasLeftChild()) {
      currentNode.setLeft(newNode);
      size++;
      return true;
      }
    else if (result > 0 && !currentNode.hasRightChild()) {
      currentNode.setRight(newNode);
      size++;
      return true;
      }

    if (result < 0) return addPasswordHelper(toAdd, currentNode.getLeft());
    else if (result > 0) return addPasswordHelper(toAdd, currentNode.getRight());

    return false;
  }

  /**
   * Removes the matching password from the tree
   * 
   * @param toRemove, the password to be removed from the tree
   * @throws NoSuchElementException if the password is not in the tree
   */
  public void removePassword(Password toRemove) {
    if (lookup(toRemove) == null) throw new NoSuchElementException("Password isn't in tree");

    if (size == 1) {
      root = null;
      size--;
      } 
    else {
      root = removePasswordHelper(toRemove, root);
      size--;
      }
  }

  /**
   * Recursive helper method to that removes the password from this BST.
   * 
   * @param toRemove,    the password to be removed from the tree
   * @param currentNode, the root of the tree we are removing from
   * @return the PasswordNode representing the NEW root of this subtree now that toRemove has been
   *         removed. This may still be currentNode, or it may have changed!
   */
  private PasswordNode removePasswordHelper(Password toRemove, PasswordNode currentNode) {
  if (isEmpty()) return null;
  
  int result = toRemove.compareTo(currentNode.getPassword(), getComparisonCriteria());

  if (result < 0) currentNode.setLeft(removePasswordHelper(toRemove, currentNode.getLeft()));

  else if (result > 0) currentNode.setRight(removePasswordHelper(toRemove, currentNode.getRight()));

  else {
    if (currentNode.numberOfChildren() == 0) return currentNode;
    else if (currentNode.numberOfChildren() == 1) {
      if (currentNode.hasLeftChild()) currentNode = currentNode.getLeft();
      else currentNode = currentNode.getRight();
      }
    
  else {
  PasswordNode predecessor = findPredecessor(currentNode);
  predecessor.setLeft(currentNode.getLeft());
  predecessor.setRight(currentNode.getRight());
  currentNode = predecessor;
  currentNode.setLeft(removePasswordHelper(predecessor.getPassword(), currentNode.getLeft()));
  }
    }

  return currentNode; //LEAVE THIS LINE AS IS

  }
  
  /**
   * Method that helps to find the predecessor inside of a BST
   * 
   * @param currentNode, the node whos predecessor needs to be found
   * @return the predecessor
   */
  private PasswordNode findPredecessor(PasswordNode currentNode) {
    currentNode = currentNode.getLeft();
    while (currentNode.hasRightChild()) currentNode = currentNode.getRight();
    return new PasswordNode(currentNode.getPassword());

}
}
