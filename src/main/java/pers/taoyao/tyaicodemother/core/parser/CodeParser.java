package pers.taoyao.tyaicodemother.core.parser;

/**
 *
 * @author admin
 * @date 2025/12/24
 * @Version v1.0
 * @description
 */
public interface CodeParser<T> {

    /**
     * 解析代码
     *
     * @param codeContent 代码内容
     * @return 解析结果
     */
    T parseCode(String codeContent);
}
