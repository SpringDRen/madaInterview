package com.rlc.http.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class RequestHeader {

  public RequestHeader(String header) throws RequestHeaderParseException {
    this.parserHeader(header);
  }

  // GET /111/112 HTTP/1.1
  // Host: localhost:8080
  // Connection: keep-alive
  // Cache-Control: max-age=0
  // Accept:
  // text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
  // Upgrade-Insecure-Requests: 1
  // User-Agent: Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML,
  // like Gecko) Chrome/49.0.2623.75 Safari/537.36
  // Accept-Encoding: gzip, deflate, sdch
  // Accept-Language: zh-CN,zh;q=0.8
  private String header;
  /**
   * 请求方法
   */
  private String method;
  /**
   * 协议版本
   */
  private String protocol;
  /**
   * 请求的URI地址
   */
  private String requestURI;
  /**
   * 请求的主机信息
   */
  private String host;
  /**
   * Http请求连接状态信息
   */
  private String connection;
  /**
   * User-Agent,用来标识代理的浏览器信息
   */
  private String userAgent;
  /**
   * 语言，Accept-Language
   */
  private String language;
  /**
   * 请求的编码格式，Accept-Encoding
   */
  private String encoding;
  /**
   * 请求的字符编码 对应HTTP请求中的Accept-Charset
   */
  private String charset;
  /**
   * 接受类型，Accept
   */
  private String accept;

  /**
   * @return the method
   */
  public String getMethod() {
    return method;
  }

  /**
   * @return the protocol
   */
  public String getProtocol() {
    return protocol;
  }

  /**
   * @return the requestURI
   */
  public String getRequestURI() {
    return requestURI;
  }

  /**
   * @return the host
   */
  public String getHost() {
    return host;
  }

  /**
   * @return the connection
   */
  public String getConnection() {
    return connection;
  }

  /**
   * @return the userAgent
   */
  public String getUserAgent() {
    return userAgent;
  }

  /**
   * @return the language
   */
  public String getLanguage() {
    return language;
  }

  /**
   * @return the encoding
   */
  public String getEncoding() {
    return encoding;
  }

  /**
   * @return the charset
   */
  public String getCharset() {
    return charset;
  }

  /**
   * @return the accept
   */
  public String getAccept() {
    return accept;
  }

  /**
   * 解析httprequest请求头信息
   * 
   * @param header
   * @throws RequestHeaderParseException
   */
  private void parserHeader(String header) throws RequestHeaderParseException {

    if (header == null || "".equals(header.trim())) {
      throw new RequestHeaderParseException("头部信息为空");
    }

    this.header = header.trim();
    BufferedReader br = new BufferedReader(new StringReader(this.header));
    try {
      while (true) {
        String line = br.readLine();
        if (line == null) {
          break;
        }
        line = line.trim();
        // 第一行，请求方法、URI、协议
        if (line.startsWith("GET")) {
          // GET /111/112 HTTP/1.1
          this.method = "GET";

          int index = line.indexOf("HTTP");
          String uri = line.substring(3 + 1, index - 1);
          this.requestURI = uri;

          String protocol = line.substring(index);
          this.protocol = protocol;
        } else if (line.startsWith("POST")) {
          // POST /111/112 HTTP/1.1
          this.method = "POST";

          int index = line.indexOf("HTTP");
          String uri = line.substring(4 + 1, index - 1);
          this.requestURI = uri;

          String protocol = line.substring(index);
          this.protocol = protocol;
        } else if (line.startsWith("Host:")) {
          // 第二行，host
          // Host: localhost:8080
          String host = line.substring("Host:".length() + 1);
          this.host = host;
        } else if (line.startsWith("Connection:")) {
          // 第三行，连接状态
          // Connection: keep-alive
          String connection = line.substring("Connection:".length() + 1);
          this.connection = connection;
        } else if (line.startsWith("Cache-Control")) {
          // 第四行，缓存控制
          // Cache-Control: max-age=0
        } else if (line.startsWith("Accept:")) {
          // 第五行，接受类型
          // Accept:
          // text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
          String accept = line.substring("Accept:".length() + 1);
          this.accept = accept;
        } else if (line.startsWith("Upgrade-Insecure-Requests:")) {
          // 第六行，不知道什么东西。。
          // Upgrade-Insecure-Requests: 1
        } else if (line.startsWith("User-Agent:")) {
          // 第七行，浏览器信息
          // User-Agent: Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36
          // (KHTML, like Gecko) Chrome/49.0.2623.75 Safari/537.36
          String agent = line.substring("User-Agent:".length() + 1);
          this.userAgent = agent;
        } else if (line.startsWith("Accept-Encoding:")) {
          // 第八行
          // Accept-Encoding: gzip, deflate, sdch
          String encoding = line.substring("Accept-Encoding:".length() + 1);
          this.encoding = encoding;
        } else if (line.startsWith("Accept-Language:")) {
          // 第九行，语言设置
          // Accept-Language: zh-CN,zh;q=0.8
          String language = line.substring("Accept-Language:".length() + 1);
          this.language = language;
        } else if (line.startsWith("Accept-Charset:")) {
          String charset = line.substring("Accept-Charset:".length() + 1);
          this.charset = charset;
        }
      }// end while
    } catch (Exception e) {
      throw new RequestHeaderParseException(e.getMessage(), e.getCause());
    } finally {
      try {
        br.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
