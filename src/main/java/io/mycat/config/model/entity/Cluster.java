package io.mycat.config.model.entity;

import java.util.ArrayList;
import java.util.List;


public class Cluster {
  private int id;
  private String name;
  private String describe;
  private String version;
  private int mode;
  private String nodecount;

  private List<Group> groupList;

  private List<Integer> tableIds = new ArrayList<>();

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescribe() {
    return describe;
  }

  public void setDescribe(String describe) {
    this.describe = describe;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public int getMode() {
    return mode;
  }

  public void setMode(int mode) {
    this.mode = mode;
  }

  public String getNodecount() {
    return nodecount;
  }

  public void setNodecount(String nodecount) {
    this.nodecount = nodecount;
  }

  public List<Group> getGroupList() {
    return groupList;
  }

  public void setGroupList(List<Group> groupList) {
    this.groupList = groupList;
  }

  public List<Integer> getTableIds() {
    return tableIds;
  }

  public void setTableIds(List<Integer> tableIds) {
    this.tableIds = tableIds;
  }
}
