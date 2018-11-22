package io.mycat.config.model.entity;

import io.mycat.config.model.rule.RuleConfig;


public class Rule {

  private Integer id;

  private String name;

  private Integer appId;

  private Integer clusterId;

  private String clusterName;

  private String fullColumnName;

  private String ruleType;

  private Integer defaultGroupId;

  public Integer getDefaultGroupId() {
    return defaultGroupId;
  }

  public void setDefaultGroupId(Integer defaultGroupId) {
    this.defaultGroupId = defaultGroupId;
  }

  private String modNum;

  private String sclieNum;

  private String effectiveTime;

  private String itemList;

  private String type;

  private Integer applicationId;

  private Object params;

  private String creator;


  public Rule() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(Integer applicationId) {
    this.applicationId = applicationId;
  }

  public Object getParams() {
    return params;
  }

  public void setParams(Object params) {
    this.params = params;
  }

  public Integer getAppId() {
    return appId;
  }

  public void setAppId(Integer appId) {
    this.appId = appId;
  }

  public Integer getClusterId() {
    return clusterId;
  }

  public void setClusterId(Integer clusterId) {
    this.clusterId = clusterId;
  }

  public String getClusterName() {
    return clusterName;
  }

  public void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }

  public String getFullColumnName() {
    return fullColumnName;
  }

  public void setFullColumnName(String fullColumnName) {
    this.fullColumnName = fullColumnName;
  }

  public String getRuleType() {
    return ruleType;
  }

  public void setRuleType(String ruleType) {
    this.ruleType = ruleType;
  }

  public String getModNum() {
    return modNum;
  }

  public void setModNum(String modNum) {
    this.modNum = modNum;
  }

  public String getSclieNum() {
    return sclieNum;
  }

  public void setSclieNum(String sclieNum) {
    this.sclieNum = sclieNum;
  }

  public String getEffectiveTime() {
    return effectiveTime;
  }

  public void setEffectiveTime(String effectiveTime) {
    this.effectiveTime = effectiveTime;
  }

  public String getItemList() {
    return itemList;
  }

  public void setItemList(String itemList) {
    this.itemList = itemList;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  @Override
  public String toString() {
    return "Rule{" + "id=" + id + ", name='" + name + '\'' + ", type='" + type + '\'' + ", applicationId="
      + applicationId + ", params=" + params + '}';
  }
}
