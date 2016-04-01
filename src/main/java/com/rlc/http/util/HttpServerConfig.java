package com.rlc.http.util;

import java.util.Map;

//import org.apache.log4j.Logger;

public final class HttpServerConfig {

  // constants
  public static final String SERVER_BY_IO = "IO";
  public static final String SERVER_BY_NIO = "NIO";

  // private static Logger log = Logger.getLogger(HttpServerConfig.class);
  /**
   * http配置文件路径
   */
  private static final String HTTP_PROPERTIES_PATH = "/http.properties";
  private static boolean isStaticInit = false;

  /**
   * http服务器端口号，默认值80
   */
  private static int HTTPSERVER_PORT = 80;
  /**
   * 资源路径，默认同目录下
   */
  private static String SOURCEDIR = "";

  /**
   * 服务器实现类型，默认IO
   */
  private static String IO_TYPE = SERVER_BY_IO;

  /**
   * 读取配置文件赋值操作，确保属性值在此段代码之前，否则会被默认值覆盖
   */
  static {
    isStaticInit = true;
    init(HTTP_PROPERTIES_PATH);
  }

  /**
   * 初始化httpserver配置
   * 
   * @param absolutePath
   */
  public static void init(String absolutePath) {
    Map<String, String> tempMap = null;
    try {
      if (isStaticInit) {
        tempMap = PropertiesUtil
            .getPropertiesMapByStream(HttpServerConfig.class
                .getResourceAsStream(absolutePath));
      } else {
        tempMap = PropertiesUtil.getPropertiesMap(absolutePath);
      }
    } catch (Exception e) {
      e.printStackTrace();
      // log.info("读取http配置文件失败", e);
      return;
    }
    if (tempMap != null) {
      // ------------- 端口号配置 start -------------
      String port = tempMap.get("httpserver.port");
      if (port != null) {
        try {
          HTTPSERVER_PORT = Integer.valueOf(port);
        } catch (NumberFormatException e) {
          System.out.println("端口号必须为整数");
          // log.info("端口号必须为整数");
        }
      } else {
        System.out.println("端口号未配置");
        // log.info("端口号未配置");
      }
      // ------------- 端口号配置 end-------------
      // ############# 资源配路径置 start #############
      String path = tempMap.get("httpserver.source");
      if (path != null) {
        SOURCEDIR = path;
      } else {
        System.out.println("资源配路径未配置");
        // log.info("资源配路径未配置");
      }
      // ############# 资源路径配置 start #############
      // ------------- 服务器实现类型径置 start -------------
      String iotype = tempMap.get("httpserver.iotype");
      if (iotype != null) {
        IO_TYPE = iotype;
      } else {
        System.out.println("服务器实现类型未配置");
        // log.info("服务器实现类型未配置");
      }
      // ------------- 服务器实现类型配置 start -------------

    }
  }

  /**
   * 返回httpserver端口
   * 
   * @return
   */
  public static int getHttpServerPort() {
    return HTTPSERVER_PORT;
  }

  /**
   * 返回资源目录
   * 
   * @return
   */
  public static String getSourcedir() {
    return SOURCEDIR;
  }

  /**
   * 返回服务器实现类型 IO 或者 NIO
   * 
   * @return
   */
  public static String getIOType() {
    return IO_TYPE;
  }

  /**
   * 不允许实例化
   */
  private HttpServerConfig() {
    throw new AssertionError();
  }
}
