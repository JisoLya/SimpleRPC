package com.liu.examplespringbootconsumer;

import com.liu.soyanrpcspringbootstarter.annotations.EnableRpc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ExampleSpringbootConsumerApplicationTests {

    @Resource
    private ExampleServiceImpl exampleServiceImpl;

    @Test
    void test01(){
        exampleServiceImpl.test();
    }
}
