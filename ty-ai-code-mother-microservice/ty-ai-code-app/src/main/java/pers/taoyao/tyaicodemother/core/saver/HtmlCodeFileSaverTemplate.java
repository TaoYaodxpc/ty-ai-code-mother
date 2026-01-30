package pers.taoyao.tyaicodemother.core.saver;

import cn.hutool.core.util.StrUtil;
import pers.taoyao.tyaicodemother.ai.model.HtmlCodeResult;
import pers.taoyao.tyaicodemother.ai.model.enums.CodeGenTypeEnum;
import pers.taoyao.tyaicodemother.exception.BusinessException;
import pers.taoyao.tyaicodemother.exception.ErrorCode;

/**
 *
 * @author admin
 * @date 2025/12/24
 * @Version v1.0
 * @description HTML 代码保存模板
 */
public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult> {
    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void saveFiles(HtmlCodeResult result, String baseDirPath) {
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
    }

    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        // HTML 代码不能为空
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML 代码不能为空");
        }
    }
}
