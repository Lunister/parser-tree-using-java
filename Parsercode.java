import java.util.LinkedList;
import java.util.Queue;

class Node {
    char op;     // Can be '+', '-', '*', '/', or '0' in the case of a leaf
    int value;   // Only relevant if the node is a leaf
    Node left;
    Node right;
}

class ExpressionParser {
    private String expression;
    private int index;

    public Node parse(String expr) {
        expression = expr.replaceAll("\\s", ""); // Remove whitespace characters
        index = 0;
        return parseExpression();
    }

    private Node parseExpression() {
        Node node = parseTerm();

        while (index < expression.length()) {
            char c = expression.charAt(index);
            if (c == '+' || c == '-') {
                index++;
                Node rightNode = parseTerm();

                Node parentNode = new Node();
                parentNode.op = c;
                parentNode.left = node;
                parentNode.right = rightNode;

                node = parentNode;
            } else {
                break;
            }
        }

        return node;
    }

    private Node parseTerm() {
        Node node = parseFactor();

        while (index < expression.length()) {

            char c = expression.charAt(index);
            
            if (c == '+' || c == '-') {
                break;
            } 
            else if (c == ')') {
                index++;
                continue;
            }
            else {
                Node rightNode = parseFactor();

                Node parentNode = new Node();
                parentNode.op = c;
                parentNode.left = node;
                parentNode.right = rightNode;

                node = parentNode;
            }
        }

        return node;
    }

    private Node parseFactor() {
        if (index >= expression.length()) {
            throw new IllegalArgumentException("Invalid expression");
        }

        char c = expression.charAt(index);
        if (c == '(') {
            index++;
            Node node = parseExpression();

            if (index >= expression.length() || expression.charAt(index) != ')') {
                throw new IllegalArgumentException("Invalid expression: mismatched parentheses");
            }
            index++;

            return node;
        } else if (Character.isDigit(c)) {
            StringBuilder sb = new StringBuilder();
            while (index < expression.length() && Character.isDigit(expression.charAt(index))) {
                sb.append(expression.charAt(index));
                index++;
            }

            Node node = new Node();
            node.op = '0';
            node.value = Integer.parseInt(sb.toString());

            return node;
        } else {
            throw new IllegalArgumentException("Invalid expression");
        }
    }

    public static void printExpression(Node node) {
        if (node == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        if (node.op == '0') {
            sb.append(node.value);
        } else {
            sb.append("(");
            printExpression(node.left);
            sb.append(node.op);
            printExpression(node.right);
            sb.append(")");
        }

        System.out.print(sb.toString());
    }

}

public class Parsercode {
    public static void main(String[] args) {
        String expression = "3+(7-(5+9))";

        ExpressionParser parser = new ExpressionParser();
        Node root = parser.parse(expression);

        // Print the tree in a pre-order traversal
        parser.printExpression(root);
    }

    private static void BFS(Node root) {
        if (root == null) {
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                Node node = queue.poll();

                System.out.print(node.op + " ");

                if (node.op == '0') {
                    System.out.print(node.value);
                } else {
                    if (node.left != null) {
                        queue.offer(node.left);
                    }
                    if (node.right != null) {
                        queue.offer(node.right);
                    }
                }
            }

            System.out.println();
        }
    }
}
