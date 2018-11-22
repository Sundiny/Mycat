package io.mycat.config.util;
import io.mycat.config.loader.rest.RestConfigLoader;
import io.mycat.config.model.entity.Cluster;
import io.mycat.config.model.entity.Group;
import io.mycat.config.model.entity.Rule;
import io.mycat.config.model.entity.User;
import io.mycat.config.model.manager.Entity;
import io.mycat.config.model.xrule.SplitType;
import io.mycat.config.model.xrule.Table;
import io.mycat.util.PropertiesUtil;
import io.mycat.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.mycat.config.model.entity.Node;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static io.mycat.config.loader.rest.RestConfigLoader.CODE_OK;


/**
 * json转化成具体的bean
 *
 * @author <a href="huangjian@xianyunsoft.com">HuangJian</a>
 * @version 1.0.0, 2018-08-21 13:35
 */
public class JsonToBean {
  public static final Properties properties;
  public static final String DEFAULT_PROPERTIES ="./manager.properties";
  private static final Logger LOGGER = LoggerFactory.getLogger(JsonToBean.class);
  static {
    properties=new PropertiesUtil().getPropertiesInfo(JsonToBean.class, DEFAULT_PROPERTIES);
  }
  public  static final String LOGIN_URI = properties.getProperty("manager.root.url");
  private final Client client;
  public JsonToBean(Client c)
  {
      this.client = c;
  }
  public Invocation.Builder newBuilderWithSession(String url)
  {
      WebTarget webTarget = this.client.target(url);
      Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON)
              .cookie(RestConfigLoader.MANAGER_TOKEN_COOKIE, (String)client.getConfiguration().getProperty(RestConfigLoader.TOKEN_PROP));
      return invocationBuilder;
  }


  /***
   * 从restful中封装成集群对象
   * @param url 集群url
   * @return
   */
  public Cluster getClientByJson(String url){
    if(StringUtils.isEmpty(url)){
      throw new RuntimeException("Cluster request address is empty.");
    }
    Response response = newBuilderWithSession(url).get();
    GenericType<Entity<Cluster>> _type = new GenericType <Entity<Cluster>>(){};
    Entity<Cluster> cluster = response.readEntity(_type);
    return cluster.getData();
  }

  /**
   * 获取规则json封装成对象
   * @param url 请求地址
   * @return
   */
 public Rule getRuleByJson(String url){
    if(url==null){
      LOGGER.error("获取结果集为空");
      throw new RuntimeException("获取结果集为空");
    }
     Response response = newBuilderWithSession(url).get();
     GenericType <Entity <Rule>> _type = new GenericType <Entity <Rule>>() {
     };
    Entity<Rule> ruleEntity = response.readEntity(_type);
    return ruleEntity.getData();
  }

  /***
   * 获取节点或组
   * @param nodeId 节点Id
   * @return
   */
  public Node getNode(Integer nodeId) {
      if (nodeId == null) {
          throw new RuntimeException("No node ID was acquired.");
      }

      String url = LOGIN_URI + "/node/" + nodeId;
      Response response = newBuilderWithSession(url).get();
      GenericType <Entity <Node>> _type = new GenericType <Entity <Node>>() {
      };
      Entity<Node> nodeEntity = response.readEntity(_type);
      return nodeEntity.getData();
  }


  public Group getGroup(Integer groupId){
    if(groupId==0){
      LOGGER.error("获取分组ID为空");
      throw new RuntimeException("获取分组ID为空");
    }
    String url=LOGIN_URI+"/node/group/"+groupId;
    Response response = newBuilderWithSession(url).get();
    GenericType<Entity<Group>> _type = new GenericType <Entity<Group>>(){};
    Entity<Group> groupEntity = response.readEntity(_type);
    if (groupEntity.getResCode() == CODE_OK) {
        return groupEntity.getData();
    }else{
        return null;
    }
  }

  public Table getTable(Integer tableId){
      if (tableId == null) {
          throw new RuntimeException("No node ID was acquired.");
      }
      if(tableId == -1){
          return mockTable();
      }
      String url = LOGIN_URI + "/table/" + tableId;
      Response response = newBuilderWithSession(url).get();
      GenericType<Entity<Table>> _type = new GenericType <Entity<Table>>(){};
      Entity<Table> table = response.readEntity(_type);
      return table.getData();
  }

  public Table mockTable(){
      Table table = new Table();
      table.setTableName("inetlivepush");
      table.setPrimaryKey("id");
      table.setAutoIncrement(false);
      table.setNeedAddLimit(true);
      table.setSplitType(SplitType.HASH);
      table.setModCount(2);
      table.setColumn("id");
      table.setClusterId(5);
      table.setDefaultGroupId(1);

      return table;
  }

  public List<Cluster> getClientList(String url){
    if(StringUtils.isEmpty(url)){
      throw new RuntimeException("Cluster request address is empty.");
    }
    Response response = newBuilderWithSession(url).get();
    GenericType<Entity<List<Cluster>>> _type = new GenericType <Entity<List<Cluster>>>(){};
    Entity<List<Cluster>> cluster = response.readEntity(_type);
    if(cluster.getResCode() == CODE_OK) {
      return cluster.getData();
    }
    return null;
  }
  /***
   * 获取数据库中的表信息
   * @param dbSet
   * @return
   */
  public Map<String,List<String>> getTableInfo(Set<String> dbSet) {
    if(dbSet.isEmpty()){
      LOGGER.error("The database name is empty.");
      throw new RuntimeException("The database name is empty.");
    }
    Map<String,List<String>> tableMap=new HashMap<>();
    for(String dbStr:dbSet) {
      String url = LOGIN_URI + "/cluster/getTable/" + dbStr;
      Response response = newBuilderWithSession(url).get();
      GenericType<Entity<List<String>>> _type = new GenericType <Entity<List<String>>>(){};
      Entity<List<String>> table = response.readEntity(_type);
      if(table.getResCode()==CODE_OK){
        tableMap.put(dbStr,table.getData());
      }
    }
    return tableMap;
  }

  /**
   * 通过数据库名和表名获取规则
   * @param dbName 数据库名
   * @param tables 表信息
   */
  public List<Rule> getRuleInfo(String dbName, String tables) {
    if(StringUtil.isEmpty(dbName) || StringUtil.isEmpty(tables)){
      LOGGER.error("The database name or table name is empty.");
      throw new RuntimeException("The database name or table name is empty.");
    }
    String url=LOGIN_URI+"/cluster/getRule/"+dbName+"."+tables;
    Response response = newBuilderWithSession(url).get();
    GenericType<Entity<List<Rule>>> _type = new GenericType <Entity<List<Rule>>>(){};
    Entity<List<Rule>> rule = response.readEntity(_type);
    if(rule.getResCode()==CODE_OK){
      return rule.getData();
    }
    return null;
  }

  public List<User> getUserList(String url) {
    if(StringUtils.isEmpty(url)){
      throw new RuntimeException("User request address is empty.");
    }
    Response response = newBuilderWithSession(url).get();
    GenericType<Entity<List<User>>> _type = new GenericType <Entity<List<User>>>(){};
    Entity<List<User>> user = response.readEntity(_type);
    if(user.getResCode()==CODE_OK){
      return user.getData();
    }
    return null;
  }
}
