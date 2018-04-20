package cn.edu.tongji.client;

import cn.edu.tongji.api.EchoService;

import java.net.InetSocketAddress;

/**
 * @author chenkangqiang
 * @data 2018/4/19
 */
public class Client {

    public static void main(String[] ags) {
        RpcImporter<EchoService> importer = new RpcImporter<>();
        EchoService echoService = importer.importer(EchoService.class,
                new InetSocketAddress("localhost", 8088));
        System.out.println(echoService.echo("Are you ok ?"));
    }
}
