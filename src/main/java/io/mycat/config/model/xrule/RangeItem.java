package io.mycat.config.model.xrule;

public class RangeItem implements StrategyItem {

    private Integer id;
    /**
     * 归属于哪个表的切分Item
     */
    private Integer tableId;

    /**
     * 路由到哪个分组
     */
    private Integer groupId;

    /**
     * 开始值
     */
    private Integer min;

    /**
     * 结束值
     */
    private Integer max;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RangeItem{");
        sb.append("tableId=").append(tableId);
        sb.append(", groupId=").append(groupId);
        sb.append(", min=").append(min);
        sb.append(", max=").append(max);
        sb.append('}');
        return sb.toString();
    }
}