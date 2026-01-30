package pers.taoyao.tyaicodemother.core.parser;

import pers.taoyao.tyaicodemother.ai.model.enums.CodeGenTypeEnum;
import pers.taoyao.tyaicodemother.exception.BusinessException;
import pers.taoyao.tyaicodemother.exception.ErrorCode;

/**
 *
 * @author admin
 * @date 2025/12/24
 * @Version v1.0
 * @description 代码解析器执行器，根据代码生产类型执行相应的解析逻辑
 */
public class CodeParserExecutor {

    private final static HtmlCodeParser htmlCodeParser = new HtmlCodeParser();

    private final static MultiCodeParser multiCodeParser = new MultiCodeParser();

    /**
     * 执行代码解析
     *
     * @param codeContent 代码内容
     * @param codeGenType 代码生成类型
     * @return 解析结果（HtmlCodeResult 或 MultiFileCodeResult）
     */
    public static Object executeParser(String codeContent, CodeGenTypeEnum codeGenType) {
        return switch (codeGenType) {
            case HTML -> htmlCodeParser.parseCode(codeContent);
            case MULTI_FILE -> multiCodeParser.parseCode(codeContent);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型: " + codeGenType);
        };
    }
}
