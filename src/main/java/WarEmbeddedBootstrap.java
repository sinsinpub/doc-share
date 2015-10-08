import com.jfinal.server.JettyWarServer;

/**
 * To start embedded Jetty server directly from WAR: java -jar doc-share.war
 * 
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
public class WarEmbeddedBootstrap {

    public static void main(String[] args) {
        int port = 80;
        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }

        String context = "/";
        if (args.length >= 2) {
            context = args[1];
        }

        JettyWarServer server = new JettyWarServer(port, context);
        server.start();
    }

}
