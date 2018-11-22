package io.mycat.config.model.entity;

import java.util.List;


public class Group {
  /**
   * 组id
   **/
  private Integer id;

  /**
   * 集群id
   **/
  private Integer clusterId;
  /**组名称*/
  private String name;
  /**节点列表**/
  private List<Node> nodes;
  /***组描述*/
  private String describes;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getClusterId() {
    return clusterId;
  }

  public void setClusterId(Integer clusterId) {
    this.clusterId = clusterId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Node> getNodes() {
    return nodes;
  }

  public void setNodes(List<Node> nodes) {
    this.nodes = nodes;
  }

  public String getDescribe() {
    return describes;
  }

  public void setDescribe(String describe) {
    this.describes = describe;
  }
}


