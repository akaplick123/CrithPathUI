package sprint.ottogroup.com.crithpathui.ui.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PathMerger {
  private Collection<Path> pathes = new ArrayList<Path>();
  private Set<Node> nodes = new TreeSet<Node>();
  private final Comparator<String> splitpotComparator = String.CASE_INSENSITIVE_ORDER;

  /**
   * @param nodes another path
   */
  public void addPath(String... nodes) {
	  addPath(Arrays.asList(nodes));
  }

  /**
   * @param nodes another path
   */
  public void addPath(List<String> nodes) {
    Node[] nodeArray = toNodes(nodes);
    Path path = new Path(nodeArray);

    this.pathes.add(path);
    this.nodes.addAll(Arrays.asList(nodeArray));
  }


  /**
   * creates a merged path
   * 
   * @return the merged path
   */
  public List<String> merge() {
    if (pathes.isEmpty()) {
      return new ArrayList<String>();
    }

    List<Node> result = new ArrayList<Node>(nodes);
    // somehow QuickSort does not work in this case
    sort(result, new MergeComparator());
    return new Path(result).toList();
  }

  public static <T> void sort(List<T> list, Comparator<? super T> c) {
    for (int i = 0; i < list.size(); i++) {
      for (int j = i + 1; j < list.size(); j++) {
        if (c.compare(list.get(i), list.get(j)) > 0) {
          // swap A and B
          T tmp = list.get(i);
          list.set(i, list.get(j));
          list.set(j, tmp);
        }
      }
    }
  }

  private Node[] toNodes(List<String> nodes) {
    if (nodes == null) {
      return new Node[] {};
    }

    Node[] result = new Node[nodes.size()];
    for (int i = 0; i < nodes.size(); i++) {
      result[i] = new Node(nodes.get(i));
    }

    return result;
  }

  private class MergeComparator implements Comparator<Node> {
    public static final int A_BEFORE_B = -1;
    public static final int A_EQUALS_B = 0;
    public static final int A_AFTER_B = 1;

    public int compare(Node a, Node b) {
      // check if both nodes are equal (e.g. same name)
      if (splitpotComparator.compare(a.getData(), b.getData()) == A_EQUALS_B) {
        return A_EQUALS_B;
      }

      // check in how many pathes is A before B and vice versa
      int cntABeforeB = cntPathRelations(a, b);
      int cntAAfterB = cntPathRelations(b, a);

      if (cntABeforeB > cntAAfterB) {
        return A_BEFORE_B;
      }
      if (cntAAfterB > cntABeforeB) {
        return A_AFTER_B;
      }

      // no real winner, so compare by name
      return splitpotComparator.compare(a.getData(), b.getData());
    }

    private int cntPathRelations(Node a, Node b) {
      int result = 0;
      for (Path path : pathes) {
        if (path.isABeforeB(a, b)) {
          result++;
        }
      }

      return result;
    }
  }

  private class Node implements Comparable<Node> {
    private final String data;

    public Node(String data) {
      this.data = data;
    }

    public String getData() {
      return data;
    }

    public int compareTo(Node o) {
      return splitpotComparator.compare(this.getData(), o.getData());
    }

    @Override
    public String toString() {
      return getData().toString();
    }
  }


  private class Path {
    private final ArrayList<Node> nodes = new ArrayList<Node>();

    public Path(Node... nodes) {
      this(Arrays.asList(nodes));
    }

    public boolean isABeforeB(Node a, Node b) {
      int nodeAFound = -1;
      int nodeBFound = -1;
      int idx = 0;

      for (Node node : nodes) {
        if (splitpotComparator.compare(node.getData(), a.getData()) == 0) {
          // we found node A
          nodeAFound = idx;
          if (nodeAFound != -1 && nodeBFound != -1) {
            // early exit
            return (nodeAFound < nodeBFound);
          }
          idx++;
        } else if (splitpotComparator.compare(node.getData(), b.getData()) == 0) {
          // we found node B
          nodeBFound = idx;
          if (nodeAFound != -1 && nodeBFound != -1) {
            // early exit
            return (nodeAFound < nodeBFound);
          }
          idx++;
        }
      }

      return false;
    }


    public Path(List<Node> nodes) {
      this.nodes.addAll(nodes);
    }

    public List<String> toList() {
      List<String> result = new ArrayList<String>();
      for (Node node : nodes) {
        result.add(node.getData());
      }
      return result;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      String delim = "";
      sb.append("path: (");
      for (Node node : nodes) {
        sb.append(delim).append(node.toString());
        delim = " -> ";
      }
      sb.append(")");
      return sb.toString();
    }
  }

}
