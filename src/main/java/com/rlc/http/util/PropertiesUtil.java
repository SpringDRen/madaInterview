package com.rlc.http.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public final class PropertiesUtil {
  
  /**
   * 不允许实例化
   */
  private PropertiesUtil() {
    throw new AssertionError();
  }

  /**
   * 根据properties路径、key值，实时读取属性值
   * 
   * @param path
   *          classpath相对路径
   * @param key
   *          property的key值
   * @return
   * @throws IOException
   */
  public static String getPropertie(String path, String key) throws IOException {

    if (path == null || "".equals(path.trim())) {
      throw new RuntimeException("属性配置文件相对路径不能为空");
    }

    if (key == null || "".equals(key.trim())) {
      throw new RuntimeException("属性的key不能为空");
    }

    Properties p = new Properties();
    InputStream is = new FileInputStream(PropertiesUtil.class.getResource(
        "/" + path).getPath());
    p.load(is);
    is.close();
    return p.getProperty(key);
  }

  /**
   * 将配置文件读入内存中
   * 
   * @param absolutePath
   *          绝对路径
   * @return
   * @throws IOException
   */
  public static Map<String, String> getPropertiesMap(String absolutePath)
      throws IOException {
    if (absolutePath == null || "".equals(absolutePath.trim())) {
      throw new RuntimeException("属性配置文件路径不能为空");
    }

    Properties p = new Properties();
    InputStream is = new FileInputStream(absolutePath);
    p.load(is);
    is.close();
    Map<String, String> reMap = new HashMap<String, String>();
    Set<Object> set = p.keySet();
    for (Object object : set) {
      if (object != null) {
        reMap
            .put(String.valueOf(object), p.getProperty(String.valueOf(object)));
      }
    }
    return reMap;
  }

}
