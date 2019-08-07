package com.springboot;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import com.springboot.utility.DateTimeUtils;
import com.zaxxer.hikari.HikariDataSource;

@SpringBootApplication
public class TshirtApiApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(TshirtApiApplication.class, args);
	}
	
	@Autowired
    private HikariDataSource hikariDataSource;

    @Profile({"local", "dev"})
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            // Display Environmental Useful Variables
            try {
                System.out.println("\n");
                Runtime runtime = Runtime.getRuntime();
                double mb = 1048576;// megabtye to byte
                double gb = 1073741824;// gigabyte to byte
                Environment env = ctx.getEnvironment();
                
                TimeZone timeZone = TimeZone.getDefault();

                System.out.println("************************ Springboot Course - TShirt ***************************");
                System.out.println("** Active Profile: " + Arrays.toString(env.getActiveProfiles()));
                System.out.println("** Port: " + env.getProperty("server.port"));
                System.out.println("** Build: " + "0.0.267");
                System.out.println("** Timezone: " + timeZone.getID());
                System.out.println("** TimeStamp: " + DateTimeUtils.getFormattedDate(new Date()));
                if (Arrays.toString(env.getActiveProfiles()).equals("[local]")) {
                    System.out.println("** Internal Url: http://" + env.getProperty("project.host") + ":" + env.getProperty("server.port"));
                    System.out.println("** External Url: http://" + InetAddress.getLocalHost().getHostAddress() + ":" + env.getProperty("server.port") );

                    System.out.println("** Internal Swagger: http://" + env.getProperty("project.host") + ":" + env.getProperty("server.port")
                            + "/swagger-ui.html");
                    System.out.println("** External Swagger: http://" + InetAddress.getLocalHost().getHostAddress() + ":" + env.getProperty("server.port")
                             + "/swagger-ui.html");

                } else {
                    System.out.println("** External Url: https://" + env.getProperty("project.host"));
                    System.out.println("** External Swagger: https://" + env.getProperty("project.host") + "/swagger-ui.html");

                }
                
                System.out.println("************************* Database - Config **********************************");
                System.out.println("** Pool Size: " + hikariDataSource.getMaximumPoolSize());
                System.out.println("** Minimum Idle Connections: "+hikariDataSource.getMinimumIdle());
                System.out.println("** Connection Timeout: "+ (hikariDataSource.getConnectionTimeout()/1000) + " seconds");
                System.out.println("** Maximum Life Time: "+ (hikariDataSource.getMaxLifetime()/1000) +" seconds");
                
                System.out.println("************************* Java - JVM *****************************************");
                System.out.println("** Number of processors: " + runtime.availableProcessors());
                String processName = ManagementFactory.getRuntimeMXBean().getName();
                System.out.println("** Process ID: "+processName.split("@")[0]);
                System.out.println("** Total memory: " + (double) (runtime.totalMemory() / mb) + " MB = " + (double) (runtime.totalMemory() / gb) + " GB");
                System.out.println("** Max memory: " + (double) (runtime.maxMemory() / mb) + " MB = " + (double) (runtime.maxMemory() / gb) + " GB");
                System.out.println("** Free memory: " + (double) (runtime.freeMemory() / mb) + " MB = " + (double) (runtime.freeMemory() / gb) + " GB");
                System.out.println("******************************************************************************");
            } catch (Exception e) {
                System.err.println("Exception, commandlineRunner -> " + e.getMessage());
            }
            System.out.println("\n");
        };
    }
}