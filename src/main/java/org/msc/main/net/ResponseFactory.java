package org.msc.main.net;

import org.msc.main.util.CommonsConstant;
import org.msc.main.util.MsgUtils;

public class ResponseFactory {

    public static Response createOkResponse(Object data){
        return new Response(CommonsConstant.OK_RESPONSE, MsgUtils.getMsg("resp.ok")).data(data);
    }

    public static Response createErrResponse(String tip){
        return new Response(CommonsConstant.ERR_RESPONSE, MsgUtils.getMsg("resp.err", tip));
    }
}
