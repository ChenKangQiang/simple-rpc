package cn.edu.tongji.service;

/**
 * @author chenkangqiang
 * @data 2018/4/19
 */
public class Server {

    public static void main(String[] args) {
        try {
            // 启动远程服务
            RpcExporter.exporter("localhost", 8088);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
