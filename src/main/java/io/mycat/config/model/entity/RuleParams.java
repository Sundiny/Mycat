package io.mycat.config.model.entity;


public class RuleParams {

  private String field;

  private String name;

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "RuleParams{" + "field='" + field + '\'' + ", name='" + name + '\'' + '}';
  }
}
