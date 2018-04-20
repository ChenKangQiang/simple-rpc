package cn.edu.tongji.client;

import cn.edu.tongji.api.RpcCallData;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * @author chenkangqiang
 * @data 2018/4/19
 */
public class RpcImporter<T> {

    public T importer(final Class<?> serviceClass, final InetSocketAddress address) {
        // 类加载器
        ClassLoader loader = serviceClass.getClassLoader();
        // 动态代理需实现的接口
        Class<?>[] interfaces = new Class<?>[] {serviceClass};
        return (T) Proxy.newProxyInstance(loader, interfaces, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = null;
                ObjectOutputStream output = null;
                ObjectInputStream input = null;
                try {
                    RpcCallData callData = new RpcCallData();
                    callData.setInterfaceName(serviceClass.getName());
                    callData.setMethodName(method.getName());
                    callData.setParameterTypes(method.getParameterTypes());
                    callData.setArguments(args);
                    socket = new Socket();
                    socket.connect(address);
                    output = new ObjectOutputStream(socket.getOutputStream());
//                    output.writeUTF(serviceClass.getName());
//                    output.writeUTF(method.getName());
//                    output.writeObject(method.getParameterTypes());
//                    output.writeObject(args);
                    output.writeObject(callData);
                    input = new ObjectInputStream(socket.getInputStream());
                    return input.readObject();
                } finally {
                    if (socket != null) {
                        socket.close();
                    }
                    if (output != null) {
                        output.close();
                    }
                    if (input != null) {
                        input.close();
                    }
                }
            }
        });
    }
}
