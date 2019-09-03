/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL.ctl;

import generators.misc.modelcheckerCTL.Util;
import generators.misc.modelcheckerCTL.token.Token;
import generators.misc.modelcheckerCTL.token.Tokenizer;
import generators.misc.modelcheckerCTL.token.Type;

import java.util.ArrayList;
import java.util.List;

public class CTLTreeBuilder {

    public CTLNode buildTree(Token[] tokens) {

        return buildTree(tokens, 0, tokens.length - 1);
    }

    public CTLNode buildTree(List<Token> tokens) {
        return buildTree(tokens.toArray(new Token[tokens.size()]));
    }

    private CTLNode buildTree(Token[] tokens, int left, int right) {

        CTLNode aNode = null;
        Type tmpUntil = null;
        CTLNode tmpLeft = null;
        CTLNode tmpRight = null;
        boolean waitingForInnerFormula = false;

        if (left <= right) {
            aNode = new CTLNode();
            for (int i = left; i <= right; i++) {
                //System.out.println(i+" = "+tokens[i]);
                if (Util.isABracket(tokens[i])) {
                    //System.out.println("Hallo " + tokens[i].toString());
                    int tempFormulaEnd = Util.indexOfClosingBracket(tokens, i);
                    if (i == left && tempFormulaEnd == right) {
                        //ignore unnecessary Brackets
                        return buildTree(tokens, i + 1, tempFormulaEnd - 1);
                    } else {
                        if (tmpLeft == null) {
                            //System.out.println("call left " + (i + 1) + " " + (tempFormulaEnd - 1));
                            tmpLeft = buildTree(tokens, i + 1, tempFormulaEnd - 1);
                        } else {
                            //System.out.println("call right " + (i + 1) + " " + (tempFormulaEnd - 1));
                            tmpRight = buildTree(tokens, i + 1, tempFormulaEnd - 1);
                        }
                    }
                    i = tempFormulaEnd;
                    waitingForInnerFormula = false;
                } else {

                    Type type = tokens[i].getType();
                    int affixOfOperator = Util.getAffixOfOperator(type);
                    //System.out.println(tokens[i]+" "+affixOfOperator);
                    if (affixOfOperator == 1 || affixOfOperator == 2) {
                        aNode.setToken(tokens[i]);
                        waitingForInnerFormula = true;
                    } else if (affixOfOperator == 3) {
                        if (tmpUntil == null) {
                            tmpUntil = type;
                        } else {
                            if (tmpUntil == Type.AU_START && type == Type.AU_END) {
                                aNode.setToken(new Token(Type.AU));
                            } else if (tmpUntil == Type.EU_START && type == Type.EU_END) {
                                aNode.setToken(new Token(Type.EU));
                            } else {
                                throw new IllegalArgumentException(tmpUntil.name() + " was followed by " + type.name());
                            }
                        }
                        waitingForInnerFormula = true;
//                        aNode.setToken(tokens[i]);
                    } else if (type == Type.TERMINAL || type == Type.TRUE || type == Type.FALSE) {
                        if (waitingForInnerFormula) {
                            if (aNode.getLeft() == null) {
                                tmpLeft = new CTLNode(tokens[i]);
                            } else {
                                tmpRight = new CTLNode(tokens[i]);

                            }
                            waitingForInnerFormula = false;
                        } else
                            aNode.setToken(tokens[i]);
                    }
                }

            }

            int affixOfOperator = Util.getAffixOfOperator(aNode.getToken().getType());
            if (affixOfOperator == 1) {
                aNode.setRight(tmpLeft);
            } else if (affixOfOperator == 2 || affixOfOperator == 3) {
                aNode.setLeft(tmpLeft);
                aNode.setRight(tmpRight);
            }



        }


        return aNode;
    }

    public CTLNode process(String str) {
        return buildTree(new Tokenizer(str).getTokens());
    }


    private void minimize(CTLNode node, Type oldType, Type newType, String leftFormula, String rightFormula) {
        if (node != null) {
            String temp;
            if (node.getToken().getType() == oldType) {
                node.setToken(new Token(newType));
                if (leftFormula != null) {
                    node.setLeft(process(leftFormula));
                }
                if (rightFormula != null) {
                    temp = rightFormula + node.getRight().toString() + ")";
                    node.setRight(process(temp));
                }
            }
            minimize(node.getLeft(), oldType, newType, leftFormula, rightFormula);
            minimize(node.getRight(), oldType, newType, leftFormula, rightFormula);
        }
    }

    public void minimizeAX(CTLNode node) {
        minimize(node, Type.AX, Type.NOT, null, "EX(!");
    }

    public void minimizeEF(CTLNode node) {
        minimize(node, Type.EF, Type.EU, "(true)", null);
    }

    public void minimizeEG(CTLNode node) {
        minimize(node, Type.EG, Type.NOT, null, "AF(!");
    }

    public void minimizeAG(CTLNode node) {
        minimize(node, Type.AG, Type.NOT, null, "EF(!");
    }

    public void minimizeAU(CTLNode node) {
        if (node != null) {
            String temp;
            if (node.getToken().getType() == Type.AU) {
                node.setToken(new Token(Type.NOT));
                String tempLeft = node.getLeft().toString();
                String tempRight = node.getRight().toString();
                temp = "((E(!" + tempRight + ")U((!" + tempLeft + ")&(!" + tempRight + ")))|(EG(!" + tempRight + ")))";
                node.setRight(process(temp));
                node.setLeft(null);

            }
            minimizeAU(node.getLeft());
            minimizeAU(node.getRight());
        }

    }

    public void normalizeNegation(CTLNode node) {
        if (node != null) {

            String temp;
            CTLNode tmp = node.getRight();
            if (node.getToken().getType() == Type.NOT && tmp != null && tmp.getToken().getType() == Type.NOT) {
                if (tmp.getRight() != null) {
                    node.setToken(tmp.getRight().getToken());
                    node.setLeft(tmp.getRight().getLeft());
                    node.setRight((tmp.getRight().getRight()));

                } else throw new IllegalArgumentException("bad formula");

            }
            normalizeNegation(node.getLeft());
            normalizeNegation(node.getRight());
        }

    }

    public List<String> minimizeAll(CTLNode node) {
        List<String> result = new ArrayList<>();
        result.add(node.toString());

        minimizeAG(node);
        normalizeNegation(node);
        signalNormalizedMinimization(Type.AG, node);
        result.add(node.toString());

        minimizeEF(node);
        normalizeNegation(node);
        signalNormalizedMinimization(Type.EF, node);
        result.add(node.toString());

        minimizeAU(node);
        normalizeNegation(node);
        signalNormalizedMinimization(Type.AU, node);
        result.add(node.toString());

        minimizeEG(node);
        normalizeNegation(node);
        signalNormalizedMinimization(Type.EG, node);
        result.add(node.toString());

        minimizeAX(node);
        normalizeNegation(node);
        signalNormalizedMinimization(Type.AX, node);
        result.add(node.toString());

        return  result;
    }

    void signalNormalizedMinimization(Type type, CTLNode node){
        //System.out.println(node.toString());
    }

    public void printTree(CTLNode node, int depth) {
        if (node != null) {
            String tabs = "";
            for (int i = 0; i < depth; i++) {
                tabs += "\t";
            }
            System.out.println(tabs + node.getToken().toString());
            printTree(node.left, depth + 1);
            printTree(node.right, depth + 1);
        }
    }

    public static void main(String[] args) {
        CTLTreeBuilder builder = new CTLTreeBuilder();
        String str = "AG((guenther) -> (!(A(true)U(!(EG(EX(herbert)))))))";
//        str = "(EF(AX((b)->(c))))";
//        str = "(A(a)U(b))";
//        str = "AF((c)->(AG(EX(EX(c)))))";
//        str = "(AG(A(a)U(b)))";
//        str = "(((a))&(b))";
        CTLNode node = builder.process(str);
        builder.minimizeAll(node);
        builder.printTree(node, 0);
        System.out.println("");
        System.out.println(node.toString());
        builder.normalizeNegation(node);
        System.out.println(node.toString());

    }

}
