package com.rlc.http.httpserver;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import com.rlc.http.util.HttpServerConfig;

public class ResponseHeader {
  /**
   * 请求响应状态
   */
  private String http_1_0_status;

  public String getStatus() {
    return http_1_0_status;
  }

  // WEB服务器表明自己是否接受获取其某个实体的一部分（比如文件的一部分）的请求。bytes：表示接受，none：表示不接受。
  // Accept-Ranges:bytes
  public static final String ACCEPT = "Accept-Ranges";

  /**
   * 默认字节
   * 
   * <pre>
   * Accept-Ranges:bytes
   * </pre>
   * 
   * @return
   */
  public String getAccept() {
    return ACCEPT + ":bytes";
  }

  // Cache-Control:max-age=0
  public static final String CACHE = "Cache-Control";

  /**
   * 默认不缓存
   * 
   * <pre>
   * Cache-Control:max-age=0
   * </pre>
   * 
   * @return
   */
  public String getCache() {
    return CACHE + ":" + "max-age=0";
  }

  // Connection:keep-alive
  public static final String CONNECTION = "Connection";

  /**
   * 默认保持连接
   * 
   * <pre>
   * Connection:keep-alive
   * </pre>
   * 
   * @return
   */
  public String getConnection() {
    return CONNECTION + ":" + "keep-alive";
  }

  // Content-Length:0
  public static final String CONTENT_LENGTH = "Content-Length";
  private long filelength;

  public String getContentLength() {
    return CONTENT_LENGTH + ":" + this.filelength;
  }

  // Content-Type:image/gif
  public static final String CONTENT_TYPE = "Content-Type";
  // Date:Thu, 31 Mar 2016 14:21:39 GMT
  public static final String DATE = "Date";
  // Expires:Thu, 31 Mar 2016 14:21:39 GMT
  public static final String EXPIRES = "Expires";

  // Last-Modified:Mon, 25 Aug 2014 02:18:09 GMT
  public static final String LAST_MODIFIED = "Last-Modified";

  /**
   * 获取资源最后修改时间，GMT格式
   * 
   * <pre>
   * Last-Modified:Mon, 25 Aug 2014 02:18:09 GMT
   * </pre>
   * 
   * @return
   */
  public String getLastmodified() {
    return LAST_MODIFIED + ":" + this.sourceLastMod;
  }

  private String sourceLastMod;

  /**
   * 默认编码，暂时写死。。
   */
  private String encoding = "UTF-8";

  private RequestHeader request;
  /**
   * 文件是否存在
   */
  private boolean sourcesIsReadable = false;
  private File sourceFile;

  public File getSourceFile() {
    return this.sourceFile;
  }

  public ResponseHeader(RequestHeader request) {
    this.request = request;
    parseRequestHeader();
  }

  /**
   * 请求的资源是否可读
   * 
   * @return
   */
  public boolean isSourcesIsReadable() {
    return this.sourcesIsReadable;
  }

  /**
   * 根据request头处理response头部信息
   * 
   */
  private void parseRequestHeader() {
    if (request == null) {
      this.http_1_0_status = "HTTP/1.0 204 error header";
    } else {
      File f;
      try {
        f = new File(HttpServerConfig.getSourcedir()
            + decodeURI(request.getRequestURI()));
      } catch (UnsupportedEncodingException e) {
        f = new File(HttpServerConfig.getSourcedir() + request.getRequestURI());
        e.printStackTrace();
      }
      if (f.exists()) {
        this.sourceFile = f;
        // this.filelength = f.length();//不读取文件长度，大概可以提高1ms的响应速度
        this.sourcesIsReadable = true;
        this.sourceLastMod = new Date(f.lastModified()).toGMTString();
        this.http_1_0_status = "HTTP/1.0 200 OK";
      } else {
        this.sourcesIsReadable = false;
        this.http_1_0_status = "HTTP/1.0 404 Not Found";
      }
    }
  }

  /**
   * 返回响应类型
   * 
   * @return
   */
  public String getContentType() {
    if (request == null) {
      return CONTENT_TYPE + ":" + HTML;
    }
    String uri;
    try {
      uri = decodeURI(request.getRequestURI());
    } catch (UnsupportedEncodingException e) {
      uri = request.getRequestURI();// 解码失败则直接处理
      e.printStackTrace();
    }
    // 简单处理文件类型。。。仅以后缀名判断
    String type = uri.substring(uri.lastIndexOf(".") + 1);
    String resultype = HTML;
    switch (type.toLowerCase()) {
    case "html":
      resultype = HTML;
      break;
    case "txt":
      resultype = TXT;
      break;
    case "js":
      resultype = JAVASCRIPT;
      break;
    case "css":
      resultype = CSS;
      break;
    case "png":
      resultype = PNG;
      break;
    case "jpeg":
      resultype = JPG;
      break;
    case "jpg":
      resultype = JPG;
      break;

    default:
      break;
    }
    return CONTENT_TYPE + ":" + resultype;
  }

  public String decodeURI(String uri) throws UnsupportedEncodingException {
    return URLDecoder.decode(uri, encoding);
  }

  public String encodeURI(String uri) throws UnsupportedEncodingException {
    return URLEncoder.encode(uri, encoding);
  }

  public static final String HTML = "text/html";
  public static final String TXT = "text/plain";
  public static final String JAVASCRIPT = "application/x-javascript";
  public static final String CSS = "text/css";
  public static final String PNG = "image/png";
  public static final String PNG_D = "application/x-png";
  public static final String JPG = "image/jpeg";
  public static final String JPG_D = "application/x-jpg";

}
