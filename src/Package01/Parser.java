package Package01;

class Parser {
    private final int NONE = 0;
    private final int SYMBOL = 1;
    private final int NUMBER = 2;

    private final int SYNTAX_ERROR = 0;
    private final int BRACKETS_ERROR = 1;
    private final int NO_EXPRESSION_ERROR = 2;
    private final int DIV_ZERO_ERROR = 3;

    private final String END_OF_EXRPESSION = "\0";

    private String expression;
    private int expression_index;
    private String token;
    private int token_type;

    private void get_token() {
        token_type = NONE;
        token = "";

        if (expression_index == expression.length()) {
            token = END_OF_EXRPESSION;
            return;
        }

        while (expression_index < expression.length() && Character.isWhitespace(expression.charAt(expression_index))) ++expression_index;

        if (expression_index == expression.length()) {
            token = END_OF_EXRPESSION;
            return;
        }
        if (is_symbol(expression.charAt(expression_index))) {
            token += expression.charAt(expression_index);
            expression_index++;
            token_type = SYMBOL;
        } else if (Character.isDigit(expression.charAt(expression_index))) {
            while (!is_symbol(expression.charAt(expression_index))) {
                token += expression.charAt(expression_index);
                expression_index++;
                if (expression_index >= expression.length()) break;
            }
            token_type = NUMBER;
        } else {
            token = END_OF_EXRPESSION;
            return;
        }
    }

    private boolean is_symbol (char c) {
        return ("+-/*()".indexOf(c) != -1);
    }

    // точка входа
    double evaluate(String expression) throws ParserException {
        double result;
        this.expression = expression;
        expression_index = 0;
        get_token();
        if (token.equals(END_OF_EXRPESSION)) handle_error(NO_EXPRESSION_ERROR);
        result = evalExp1();
        if (!token.equals(END_OF_EXRPESSION)) handle_error(SYNTAX_ERROR);
        return result;
    }

    // сложение, вычитание
    private double evalExp1() throws ParserException {
        char op;
        double result;
        double partial_result;
        result = evalExp2();
        while ((op = token.charAt(0)) == '+' || op == '-') {
            get_token();
            partial_result = evalExp2();
            switch (op) {
                case '-':
                    result = result - partial_result;
                    break;
                case '+':
                    result = result + partial_result;
                    break;
            }
        }
        return result;
    }

    // умножение, деление
    private double evalExp2() throws ParserException {
        char op;
        double result;
        double partial_result;
        result = evalExp3();
        while ((op = token.charAt(0)) == '*' || op == '/') {
            get_token();
            partial_result = evalExp3();
            switch (op) {
                case '*':
                    result = result * partial_result;
                    break;
                case '/':
                    if (partial_result == 0.0)
                        handle_error(DIV_ZERO_ERROR);
                    result = result / partial_result;
                    break;
            }
        }
        return result;
    }

    // положительное, отрицательное
    private double evalExp3() throws ParserException {
        double result;
        String op;
        op = " ";
        if ((token_type == SYMBOL) && token.equals("+") || token.equals("-")) {
            op = token;
            get_token();
        }
        result = evalExp4();
        if (op.equals("-")) result = -result;
        return result;
    }

    // скобки
    private double evalExp4() throws ParserException {
        double result;
        if (token.equals("(")) {
            get_token();
            result = evalExp1();
            if (!token.equals(")"))
                handle_error(BRACKETS_ERROR);
            get_token();
        }
        else result = get_number();
        return result;
    }

    private double get_number() throws ParserException {
        double result = 0.0;
        switch (token_type) {
            case NUMBER:
                try {
                    result = Double.parseDouble(token);
                } catch (NumberFormatException exc) {
                    handle_error(SYNTAX_ERROR);
                }
                get_token();
                break;
            default:
                handle_error(SYNTAX_ERROR);
                break;
        }
        return result;
    }

    private void handle_error (int error) throws ParserException {
        String[] err = {
                "Ошибка в синтаксисе.",
                "Неправильно расставлены скобки.",
                "Нечего вычислять.",
                "Делить на ноль нельзя!"
        };
        throw new ParserException(err[error]);
    }
}