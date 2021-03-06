package org.combo.app.exception;

import org.combo.app.util.MsgUtils;

public class ExceptionFactory {

    /**
     * 创建一个业务异常
     * @param code ErrorCode里的值
     * @param overrides
     * @return
     */
    public static BusinessException create(int code, Object... overrides) {
        String msg = MsgUtils.getMsg("err." + code, overrides);
        return new BusinessException(code, msg);
    }
}
