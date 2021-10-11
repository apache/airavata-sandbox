package org.apache.airavata.jupyter.container.magic.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"org.apache.airavata.jupyter"})
@SpringBootApplication()
public class Application implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
