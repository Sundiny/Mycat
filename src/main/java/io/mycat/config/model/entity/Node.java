package io.mycat.config.model.entity;


public class Node {

  /**标识ID**/
  private Integer id;
  /**节点ip**/
  private String ip;

  private Integer isMaster;
  /**端口号**/
  private Integer port;
  /**所在组ID**/
  private Integer groupId;
  /**节点数据库用户名**/
  private String dbUsername;
  /**节点数据库密码**/
  private String dbPassword;
  /**数据库名**/
  private String schema;
  /**节点创建时间**/
  private String gmtCreated;
  /**节点更改时间**/
  private String gmtUpdated;

  private Integer weight;

  private Integer state;

  private String name;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Integer getIsMaster() {
    return isMaster;
  }

  public void setIsMaster(Integer isMaster) {
    this.isMaster = isMaster;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public Integer getGroupId() {
    return groupId;
  }

  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  public String getDbUsername() {
    return dbUsername;
  }

  public void setDbUsername(String dbUsername) {
    this.dbUsername = dbUsername;
  }

  public String getDbPassword() {
    return dbPassword;
  }

  public void setDbPassword(String dbPassword) {
    this.dbPassword = dbPassword;
  }

  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public String getGmtCreated() {
    return gmtCreated;
  }

  public void setGmtCreated(String gmtCreated) {
    this.gmtCreated = gmtCreated;
  }

  public String getGmtUpdated() {
    return gmtUpdated;
  }

  public void setGmtUpdated(String gmtUpdated) {
    this.gmtUpdated = gmtUpdated;
  }

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }


  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
