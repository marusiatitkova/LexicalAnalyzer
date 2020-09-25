public enum TokenType {
    PREPROCESSOR_DIRECTIVE,
    NUMBER,
    FLOATING_POINT_NUMBER,
    HEXADECIMAL,
    STRING,
    CHARACTER,
    IDENTIFIER,
    KEYWORD,
    BLOCK_COMMENT,
    LINE_COMMENT,
    WHITESPACE,
    SEPARATOR,
    OPERATOR;

    public boolean isAuxiliary() {
        return this == BLOCK_COMMENT || this == LINE_COMMENT || this == WHITESPACE;
    }
}
