package com.jfinal.server;

/**
 * 其实就是原来JFinal的启动入口，只不过怕你不会去jar包里找这个main...
 * 
 * @see com.jfinal.core.JFinal
 */
public class JFinalMain {

    /**
     * Run JFinal Server with Debug Configurations or Run Configurations in Eclipse JavaEE args
     * example: src/main/webapp 80 / 5
     */
    public static void main(String[] args) {
        IServer server = null;
        if (args == null || args.length == 0) {
            // 默认情况ServerFactory会无法在Maven目录结构下检测到WEB-INF，所以直接指定
            server = ServerFactory.getServer("src/main/webapp", 80, "/", 5);
            server.start();
        } else {
            String webAppDir = args[0];
            int port = Integer.parseInt(args[1]);
            String context = args[2];
            int scanIntervalSeconds = Integer.parseInt(args[3]);
            server = ServerFactory.getServer(webAppDir, port, context, scanIntervalSeconds);
            server.start();
        }
    }

}
