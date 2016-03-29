package com.rlc.http.util;

import java.util.Map;

import org.apache.log4j.Logger;

public class HttpServerConfig {

  private static Logger log = Logger.getLogger(HttpServerConfig.class);
  /**
   * http配置文件路径
   */
  private static final String HTTP_PROPERTIES_PATH = HttpServerConfig.class.getResource(
      "/" + "http.properties").getPath() ;

  /**
   * http服务器端口号，默认值80
   */
  private static int HTTPSERVER_PORT = 80;

  /**
   * 读取配置文件赋值操作，确保属性值在此段代码之前，否则会被默认值覆盖
   */
  static {
    init(HTTP_PROPERTIES_PATH);
  }
  
  /**
   * 初始化httpserver配置
   * @param absolutePath
   */
  public static void init(String absolutePath) {
    Map<String, String> tempMap = null;
    try {
      tempMap = PropertiesUtil.getPropertiesMap(absolutePath);
    } catch (Exception e) {
      e.printStackTrace();
      log.info("读取http配置文件失败", e);
      return;
    }
    if (tempMap != null) {
      // -------------端口号配置 start-------------
      String port = tempMap.get("httpserver.port");
      if (port != null) {
        try {
          HTTPSERVER_PORT = Integer.valueOf(port);
        } catch (NumberFormatException e) {
          System.out.println("端口号必须为整数");
          log.info("端口号必须为整数");
        }
      } else {
        System.out.println("端口号未配置");
        log.info("端口号未配置");
      }
      // -------------端口号配置 end-------------
    }
  }
  
  /**
   * 返回httpserver端口
   * @return
   */
  public static int getHttpServerPort() {
    return HTTPSERVER_PORT;
  }

  private HttpServerConfig() {
    throw new RuntimeException("不允许创建对象");
  }
}
