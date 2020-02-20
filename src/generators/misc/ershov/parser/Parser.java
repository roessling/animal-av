package generators.misc.ershov.parser;

import generators.misc.ershov.TreeNode;
import generators.misc.ershov.parser.exception.ParserException;

import java.util.List;

public class Parser {
    private Tokenizer tokenizer = new Tokenizer();

    private Token lookahead;

    private List<Token> tokens;

    private int lookaheadIndex = -1;

    public TreeNode parse(String expression) {
        tokens = tokenizer.tokenize(expression);
        lookaheadIndex = -1;
        nextToken();
        TreeNode expr = expr();

        if(lookahead.getTokenType() != TokenType.EPSILON)
            throw new ParserException("Finished parsing but got tokens left");

        return expr;
    }

    private void nextToken() {
        lookaheadIndex++;
        if (lookaheadIndex < tokens.size()) {
            lookahead = tokens.get(lookaheadIndex);
        } else {
            lookahead = new Token(TokenType.EPSILON, "");
        }
    }

    private TreeNode expr() {
        TreeNode left = signedTerm();
        TreeNode node = sumOp(left);
        if (node == null) {
            return left;
        } else {
            return node;
        }
    }

    private TreeNode signedTerm() {
        if (TokenType.PLUS_MINUS == lookahead.getTokenType()) {
            String key = lookahead.getSequence();
            nextToken();
            return new TreeNode(key, null, term());
        }
        return term();
    }

    private TreeNode sumOp(TreeNode left) {
        if (TokenType.PLUS_MINUS == lookahead.getTokenType()) {
            String key = lookahead.getSequence();
            nextToken();
            TreeNode node = term();
            TreeNode leftNode = new TreeNode(key, left, node);
            TreeNode restNode = sumOp(leftNode);
            if (restNode == null) {
                return leftNode;
            } else {
                return restNode;
            }
        } else {
            return null;
        }
    }

    private TreeNode term() {
        TreeNode left = argument();
        TreeNode node = termOp(left);
        if (node == null) {
            return left;
        } else {
            return node;
        }
    }

    private TreeNode argument() {
        if (TokenType.OPENING_BRACE == lookahead.getTokenType()) {
            nextToken();
            TreeNode node = expr();
            if (TokenType.CLOSING_BRACE == lookahead.getTokenType()) {
                nextToken();
                return node;
            } else {
                // Parsing Error
                throw new ParserException("I expected a closing brace, but I found " + lookahead.getSequence() + " (with type: " + lookahead.getTokenType() + ")");
            }
        } else if (TokenType.NUMBER == lookahead.getTokenType() || TokenType.VARIABLE == lookahead.getTokenType()) {
            String key = lookahead.getSequence();
            nextToken();
            return new TreeNode(key, null, null);
        } else {
            // Parsing Error
            throw new ParserException("I expected a opening brace, a number or a variable, but I found " + lookahead.getSequence() + " (with type: " + lookahead.getTokenType() + ")");
        }
    }

    private TreeNode termOp(TreeNode left) {
        if (TokenType.MUL_DIV == lookahead.getTokenType()) {
            String key = lookahead.getSequence();
            nextToken();
            TreeNode right = signedArgument();
            TreeNode node = new TreeNode(key, left, right);
            TreeNode restOver = termOp(node);
            if (restOver == null) {
                return node;
            } else {
                return restOver;
            }
        } else {
            return null;
        }
    }

    private TreeNode signedArgument() {
        if (TokenType.PLUS_MINUS == lookahead.getTokenType()) {
            String key = lookahead.getSequence();
            nextToken();
            return new TreeNode(key, null, argument());
        }
        return argument();
    }
}
