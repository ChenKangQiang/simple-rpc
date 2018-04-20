package cn.edu.tongji.api;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenkangqiang
 * @data 2018/4/19
 */
@Data
public class RpcCallData implements Serializable {
    private static final long serialVersionUID = -8959100002938918291L;
    /**
     *  远程服务的接口名
     */
    private String interfaceName;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 方法的参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 方法的参数
     */
   private Object[] arguments;
}
