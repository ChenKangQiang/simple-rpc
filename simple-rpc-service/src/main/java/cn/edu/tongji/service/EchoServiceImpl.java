package cn.edu.tongji.service;

import cn.edu.tongji.api.EchoService;

/**
 * @author chenkangqiang
 * @data 2018/4/19
 */
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String ping) {
        return ping != null ? ping + " --> I am OK. " : " I am OK. ";
    }

}
