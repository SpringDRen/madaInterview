package com.rlc.http.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.rlc.http.util.HttpServerConfig;

public class HttpServerConfigTest {

  @Test
  public void testClassDefault() {
    //将classpath路径下的http.properties删除即可测试
    //assertTrue(HttpServerConfig.getHttpServerPort() == 80);
  }
  
  @Test
  public void testDefaultConfig() {
    int port = HttpServerConfig.getHttpServerPort();
    System.out.println(port);
    assertTrue(port == 80);
  }
  
  @Test
  public void testPathConfig() {
    HttpServerConfig.init("E:/thing-for-work/allworks/MyWork/madaInterview/src/test/resources/httptest.properties");
    int port = HttpServerConfig.getHttpServerPort();
    System.out.println(port);
    assertTrue(port == 9090);
  }

}
