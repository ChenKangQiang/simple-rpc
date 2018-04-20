package cn.edu.tongji.service;

import cn.edu.tongji.api.RpcCallData;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author chenkangqiang
 * @data 2018/4/19
 */
public class RpcExporter {

    private static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static ApplicationContext context =
            new ClassPathXmlApplicationContext("classpath:spring/config/spring-context.xml");

    public static void exporter(String hostName, int port) throws Exception {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(hostName, port));

        try {
            while (true) {
                executor.execute(new ExporterTask(serverSocket.accept()));
            }
        } finally {
            serverSocket.close();
        }
    }

    private static class ExporterTask implements Runnable {

        private Socket client = null;
        private ObjectInputStream input = null;
        private ObjectOutputStream output = null;

        public ExporterTask(Socket client) {
            this.client = client;
        }

        public void close() {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            try {
                input = new ObjectInputStream(client.getInputStream());
                RpcCallData callData = (RpcCallData) input.readObject();
                // 客户端需要远程调用的接口名
                String interfaceName = callData.getInterfaceName();
                // 需要根据接口名获取相应的实现类，略麻烦
                interfaceName = "cn.edu.tongji.service.EchoServiceImpl";
                Class<?> service = Class.forName(interfaceName);
                String methodName = callData.getMethodName();
                Class<?>[] parameterTypes = callData.getParameterTypes();
                Object[] arguments = callData.getArguments();
                Method method = service.getMethod(methodName, parameterTypes);
                Object result = method.invoke(service.newInstance(), arguments);

                output = new ObjectOutputStream(client.getOutputStream());
                output.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }
    }



}
