package io.mycat.config.model.entity;

import sun.security.util.Password;


public class User {
  /**用户id*/
  private Integer id;
  /**用户昵称*/
  private String nickname;
  /**用户名*/
  private String username;
  /**用户密码*/
  private String password;
  /**用户手机号*/
  private Integer phone;
  /**用户*/
  private String dept;
  /**用户邮箱*/
  private String email;

  private String type;

  private Integer delflag;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Integer getPhone() {
    return phone;
  }

  public void setPhone(Integer phone) {
    this.phone = phone;
  }

  public String getDept() {
    return dept;
  }

  public void setDept(String dept) {
    this.dept = dept;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getDelflag() {
    return delflag;
  }

  public void setDelflag(Integer delflag) {
    this.delflag = delflag;
  }
}
