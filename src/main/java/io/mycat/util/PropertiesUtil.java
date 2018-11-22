package io.mycat.util;

import javax.ws.rs.core.Response;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件读取工具类
 *
 * @author <a href="jian.huang@bintools.cn">HuangJian</a>
 * @version 1.0.0, 2018-09-01 17:17
 */
public class PropertiesUtil {

  public Properties getPropertiesInfo(Class className,String fileName) {
    Properties properties = new Properties();
    try {
      InputStream in = className.getClassLoader().getResourceAsStream(fileName);
      // 使用properties对象加载输入流
      properties.load(in);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return properties;
  }
}
