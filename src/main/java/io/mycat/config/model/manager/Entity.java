package io.mycat.config.model.manager;



public class Entity<T>{

  private T data;

  private String resMsg;

  private int resCode;

  public Entity(T data, String resMsg, int resCode) {
    this.data = data;
    this.resMsg = resMsg;
    this.resCode = resCode;
  }


  public Entity() {
  }

 public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public String getResMsg() {
    return resMsg;
  }

  public void setResMsg(String resMsg) {
    this.resMsg = resMsg;
  }

  public int getResCode() {
    return resCode;
  }

  public void setResCode(int resCode) {
    this.resCode = resCode;
  }
}
