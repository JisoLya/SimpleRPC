package com.liu.examplespringbootconsumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;


@SpringBootTest
class ExampleSpringbootConsumerApplicationTests {

    @javax.annotation.Resource
    private ExampleServiceImpl exampleServiceImpl;

    @Test
    void test01(){
        exampleServiceImpl.test();
    }
    @Test
    public void test2() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String pattern = "classpath*:application*";
        Resource[] resources = resolver.getResources(pattern);
        for(Resource resource : resources){
            System.out.println(resource);
            System.out.println(resource.getFilename());
        }
    }
}
