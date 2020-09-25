import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexeme {
    private Map<TokenType, String> regEx;
    private ArrayList<Token> tokens;
    private boolean isKeyword;
    private boolean isDirective;

    private final ArrayList<String> keywords = new ArrayList<>(Arrays.asList("char", "int", "long", "bool", "typedef", "short",
            "float", "double", "enum", "struct", "union", "signed", "unsigned", "void", "const", "auto", "extern",
            "register", "static", "for", "while", "do", "if", "else", "switch", "case", "continue", "goto", "break",
            "return", "default", "sizeof", "true", "false", "class"));

//    private final ArrayList<String> directives = new ArrayList<>(Arrays.asList("define", "error", "import", "undef", "elif",
//            "if", "include", "using", "else", "ifdef", "line", "endif", "ifndef", "pragma"));

    public Lexeme(){
        this.regEx = new TreeMap<TokenType, String>();
        launchRegEx();
        this.tokens = new ArrayList<>();
    }

    private Token separateToken(String source, int fromIndex) throws AnalyzerException {
        if (fromIndex < 0 || fromIndex > source.length()) {
            throw new IllegalArgumentException("Illegal index in the input stream!");
        }

        for (TokenType tokenType : TokenType.values()) {
//            Pattern p = Pattern.compile(".{" + fromIndex + "}" + regEx.get(tokenType),
//                    Pattern.DOTALL);
            Pattern p = Pattern.compile(regEx.get(tokenType));

            Matcher m = p.matcher(source);
            if(m.lookingAt()){ //find(fromIndex) lookingAt()
                String lexeme = m.group(0);
                Token newToken = new Token(fromIndex, fromIndex + lexeme.length(), lexeme, tokenType);
                if((tokenType == TokenType.NUMBER || tokenType == TokenType.FLOATING_POINT_NUMBER ||
                        tokenType == TokenType.HEXADECIMAL) && !checkForNextSymbol(source, lexeme, fromIndex)){
                    throw new AnalyzerException("wrong lexeme", fromIndex);
                }
                if(tokenType == TokenType.IDENTIFIER && isKeyword(newToken)){
                    newToken.setTokenType(TokenType.KEYWORD);
                }
                return newToken;
            }
        }
        return null;
    }

    private boolean checkForNextSymbol(String source, String word, int position){
//        char first = word.toCharArray()[0];
//        int posInCode = source.indexOf(first, position);
//        int w = word.length();
//        char actual = source.charAt(posInCode + w);
//        String acceptable = "=+-!><%;,.(){}[] ";
//        for(int i = 0; i < acceptable.length(); i++){
//            char expected = acceptable.charAt(i);
//            if(actual == expected){
//                return true;
//            }
//        }
//        return false;


        int w = word.length();
        char actual = source.charAt(w);
        String acceptable = "=+-!><%;,.(){}[] ";
        for(int i = 0; i < acceptable.length(); i++){
            char expected = acceptable.charAt(i);
            if(actual == expected){
                return true;
            }
        }
        return false;
    }

    public void tokenize(String source){
        int position = 0;
        Token token = null;
        String newsource = source;
        try {
            do {
                newsource = newsource.substring(position);
                //token = separateToken(source, position);
                token = separateToken(newsource, 0);
                if (token != null) {
                    position = token.getEnd();

                    if(token.getTokenType() != TokenType.WHITESPACE)
                        tokens.add(token);
                }

            } while (token != null && position != source.length());
        if (position != source.length())
            throw new AnalyzerException("Lexical error at position # " + position, position);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        catch (AnalyzerException e){
            e.getMessage();
        }
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    private void launchRegEx() {
        regEx.put(TokenType.PREPROCESSOR_DIRECTIVE, "#\\w+");
        regEx.put(TokenType.NUMBER, "\\d+");   // ([ ;,.(){}\[\]=\+\-!><%])$
        regEx.put(TokenType.FLOATING_POINT_NUMBER, "\\d+[.]\\d+(e[+-]?)?\\d+ $");
        regEx.put(TokenType.HEXADECIMAL, "0[xX]([0-9a-fA-F])+ $");
        regEx.put(TokenType.CHARACTER, "'[^']'");
        regEx.put(TokenType.STRING, "\\\"[^\"]*\\\"");
        regEx.put(TokenType.LINE_COMMENT, "//(.*)");
        regEx.put(TokenType.BLOCK_COMMENT, "/\\*[^\\*]*\\*/");
        regEx.put(TokenType.WHITESPACE, "\\s");
        regEx.put(TokenType.SEPARATOR, "[;,.(){}\\[\\]]");
        regEx.put(TokenType.OPERATOR, "[=\\+\\-!><%]");
        regEx.put(TokenType.IDENTIFIER, "[a-zA-Z_]+");
        regEx.put(TokenType.KEYWORD, "[a-zA-Z]+");
    }

    public boolean isKeyword(Token token){
        isKeyword = false;
        String keyword = token.getTokenString();
        if(keywords.contains(keyword))
            isKeyword = true;
        return isKeyword;
    }

//    public boolean isDirective(Token token){
//        isDirective = false;
//        String directive = token.getTokenString().substring(1);
//        if(directives.contains(directive))
//            isDirective = true;
//        return isDirective;
//    }
}
