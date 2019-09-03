/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL.ctl;

import generators.misc.modelcheckerCTL.token.Token;
import generators.misc.modelcheckerCTL.token.Type;

public class CTLNode {
    CTLNode left = null;
    CTLNode right = null;
    Token token;

    public CTLNode(){

    }

    public CTLNode(Token token){
        this.setToken(token);
    }

    public CTLNode(String token){
        this.setToken(new Token(Type.TERMINAL, token));
    }

    public CTLNode(CTLNode left, CTLNode right, Token token){
        this.setLeft(left);
        this.setRight(right);
        this.setToken(token);
    }

    public CTLNode getLeft() {
        return left;
    }

    public void setLeft(CTLNode left) {
        this.left = left;
    }

    public CTLNode getRight() {
        return right;
    }

    public void setRight(CTLNode right) {
        this.right = right;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public String toString(){
        String leftString = this.left == null ? "" : this.left.toString() + "";
        String rightString = this.right == null ? "" : "" + this.right.toString();
        String formula = this.token == null ? "" : "" + this.token.toString();
        String result;
        if(this.token.getType()==Type.AU){
            result="A"+leftString+"U"+rightString;
        }else if(this.token.getType()==Type.EU){
            result="E"+leftString+"U"+rightString;
        }else result=leftString+formula+rightString;

        return "(" +result+ ")";
    }

    public static void main(String[] args) {
        CTLNode aTree = new CTLNode("+");
        aTree.setLeft(new CTLNode("!"));
        aTree.getLeft().setRight(new CTLNode("a"));
        aTree.setRight(new CTLNode("-"));
        aTree.getRight().setLeft(new CTLNode("c"));
        aTree.getRight().setRight(new CTLNode("d"));

//        System.out.println(aTree.toString());

    }

}
