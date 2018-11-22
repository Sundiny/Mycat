package io.mycat.config.model.xrule;

/**
 * Created by xueguangshun on 25/08/2018.
 */
public class Table {
    /**
     * 表id
     */
    private Integer id;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 分片规则类型
     */
    private SplitType splitType;

    /**
     * 列名称
     */
    private String column;

    /**
     * 默认路由到的groupId
     */
    private Integer defaultGroupId;

    /**
     * 所属集群
     */
    private Integer clusterId;

    /**
     * 表的分片数量， 只有在取模或hash的时候才会用到
     */
    private Integer modCount;

    /**
     * 主键
     */
    private String primaryKey;

    /**
     * 是否自增
     */
    private boolean autoIncrement;

    /**
     * 是否需要添加limit
     */
    private boolean needAddLimit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public void setSplitType(SplitType splitType) {
        this.splitType = splitType;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Integer getDefaultGroupId() {
        return defaultGroupId;
    }

    public void setDefaultGroupId(Integer defaultGroupId) {
        this.defaultGroupId = defaultGroupId;
    }

    public Integer getModCount() {
        return modCount;
    }

    public void setModCount(Integer modCount) {
        this.modCount = modCount;
    }

    public Integer getClusterId() {
        return clusterId;
    }

    public void setClusterId(Integer clusterId) {
        this.clusterId = clusterId;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean isNeedAddLimit() {
        return needAddLimit;
    }

    public void setNeedAddLimit(boolean needAddLimit) {
        this.needAddLimit = needAddLimit;
    }

}
