package hello.embed;

import hello.servlet.HelloServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class EmbedTomcatServletMain {
    public static void main(String[] args) throws LifecycleException {
        System.out.println("EmbedTomcatServletMain.main");

        //톰캣 설정
        Tomcat tomcat = new Tomcat();
        Connector connector = new Connector();
        connector.setPort(80);
        tomcat.setConnector(connector);

        Context context = tomcat.addContext("", "/");

        /*
        Caused by: java.lang.IllegalArgumentException: The main resource set specified [...\tomcat\tomcat.8080\webapps] is not valid 가 발생하면
        tomcat.8080/webapps 추가하거나 아래 코드 추가
        */
        File docBaseFile = new File(context.getDocBase());
        if(!docBaseFile.isAbsolute()) {
            docBaseFile = new File(((org.apache.catalina.Host) context.getParent()).getAppBaseFile(), docBaseFile.getPath());
        }
        docBaseFile.mkdirs();

        //서블릿을 등록
        tomcat.addServlet("", "helloServlet", new HelloServlet());
        //등록한 서블릿의 경로를 매핑
        context.addServletMappingDecoded("/hello-servlet", "helloServlet");
        tomcat.start();
    }
}
