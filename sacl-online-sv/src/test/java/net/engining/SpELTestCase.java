package net.engining;

import cn.hutool.core.lang.Console;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author : Eric Lu
 * @version :
 * @date : 2021-11-11 13:10
 * @since :
 **/
public class SpELTestCase {

    public static void main(String[] args) {
        ExpressionParser parser=new SpelExpressionParser();
        Expression expression=parser.parseExpression("'Hello SPEL'");
        Console.log(expression.getExpressionString());
    }
}
