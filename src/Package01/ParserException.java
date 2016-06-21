package Package01;

class ParserException extends Exception{
    private String errStr;
    ParserException(String errStr) {
        this.errStr = errStr;
    }
    public String toString() {
        return errStr;
    }
}
