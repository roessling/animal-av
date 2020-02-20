package generators.misc.ershov.parser;

import generators.misc.ershov.parser.exception.TokenizerException;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Tokenizer {
    private class TokenInfo {
        TokenType type;
        Pattern regex;

        TokenInfo(TokenType type, Pattern regex) {
            this.type = type;
            this.regex = regex;
        }
    }

    private LinkedList<TokenInfo> tokenInfos = new LinkedList<>();

    Tokenizer() {
        tokenInfos.add(new TokenInfo(TokenType.PLUS_MINUS, Pattern.compile("^[+-]")));
        tokenInfos.add(new TokenInfo(TokenType.MUL_DIV, Pattern.compile("^[*/]")));
        tokenInfos.add(new TokenInfo(TokenType.VARIABLE, Pattern.compile("^[a-zA-Z]+")));
        tokenInfos.add(new TokenInfo(TokenType.NUMBER, Pattern.compile("^[0-9]+")));
        tokenInfos.add(new TokenInfo(TokenType.OPENING_BRACE, Pattern.compile("^[(]")));
        tokenInfos.add(new TokenInfo(TokenType.CLOSING_BRACE, Pattern.compile("^[)]")));
        tokenInfos.add(new TokenInfo(TokenType.SPACE, Pattern.compile("^[ ]")));
    }

    List<Token> tokenize(String expression) {
        LinkedList<Token> tokens = new LinkedList<>();

        boolean match;
        while (!expression.equals("")) {
            match = false;
            for (TokenInfo tokenInfo : tokenInfos) {
                Matcher matcher = tokenInfo.regex.matcher(expression);
                if (matcher.find()) {
                    match = true;

                    if (tokenInfo.type != TokenType.SPACE)
                        tokens.add(new Token(tokenInfo.type, matcher.group()));

                    expression = matcher.replaceFirst("");
                    break;
                }
            }

            if (!match)
                throw new TokenizerException(expression);
        }

        return tokens;
    }
}
