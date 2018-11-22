package io.mycat.config.model.xrule;


public class HashItem implements StrategyItem {
    private Integer id;

    /**
     * 列名称
     */
    private String tableId;

    /**
     * 模后的值
     */
    private Integer modNumber;

    /**
     * 归属的分组
     */
    private Integer groupId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public Integer getModNumber() {
        return modNumber;
    }

    public void setModNumber(Integer modNumber) {
        this.modNumber = modNumber;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
