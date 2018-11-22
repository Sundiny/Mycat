package io.mycat.config.loader.rest;

import io.mycat.config.loader.ConfigLoader;
import io.mycat.config.model.ClusterConfig;
import io.mycat.config.model.DBHostConfig;
import io.mycat.config.model.DataHostConfig;
import io.mycat.config.model.DataNodeConfig;
import io.mycat.config.model.FirewallConfig;
import io.mycat.config.model.SchemaConfig;
import io.mycat.config.model.SystemConfig;
import io.mycat.config.model.TableConfig;
import io.mycat.config.model.UserConfig;
import io.mycat.config.model.UserPrivilegesConfig;
import io.mycat.config.model.entity.Cluster;
import io.mycat.config.model.entity.Group;
import io.mycat.config.model.entity.Node;
import io.mycat.config.model.entity.Rule;
import io.mycat.config.model.entity.User;
import io.mycat.config.model.manager.Entity;
import io.mycat.config.model.rule.RuleConfig;
import io.mycat.config.model.rule.TableRuleConfig;
import io.mycat.config.model.xrule.SplitType;
import io.mycat.config.model.xrule.SplitType2Algorithm;
import io.mycat.config.util.JsonToBean;
import io.mycat.config.util.ParameterMapping;
import io.mycat.route.function.AbstractPartitionAlgorithm;
import io.mycat.util.PropertiesUtil;
import io.mycat.util.SplitUtil;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
/**
 * 采用restful API形式从Manager获取配置数据
 *
 */
public class RestConfigLoader implements ConfigLoader {
	public static final String DEFAULT_PROPERTIES ="./manager.properties";
	public static final String TOKEN_PROP = "";
	public static final String MANAGER_TOKEN_COOKIE="JSESSIONID";
	public static final int CODE_OK = 10000;
	public static final int CODE_EXPIRED = -1111;
	public static final Properties properties;
	private  Map<String, TableRuleConfig> tableRules=new HashMap<String, TableRuleConfig>();
	private  Map<String, DataHostConfig> dataHosts=new LinkedHashMap<>();
	private  Map<String, DataNodeConfig> dataNodes=new HashMap<String, DataNodeConfig>();
	private  Map<String, SchemaConfig> schemas=new HashMap<String, SchemaConfig>();
	private final Map<String, UserConfig> users=new HashMap<>();
	private static final Logger LOGGER = LoggerFactory.getLogger(RestConfigLoader.class);
	static {
		 properties=new PropertiesUtil().getPropertiesInfo(RestConfigLoader.class,DEFAULT_PROPERTIES);
	}
	public  static final String MANAGER_ROOT_URI = properties.getProperty("manager.root.url");

	@Override
	public SchemaConfig getSchemaConfig(String schema)  {
		Client client = createLoginedClient();
		JsonToBean bean=new JsonToBean(client);
		Cluster cluster=bean.getClientByJson(schema);
		if(cluster.getGroupList().size()==0){
			return null;
		}

		//获取集群中的组信息
		String groupName=getGroupInfo(bean,cluster);

		Set<String> databaseSet=new HashSet();
		for(String key:dataNodes.keySet()){
			String db=dataNodes.get(key).getDatabase();
			databaseSet.add(db);
		}

		Map<String, TableConfig> tableConfigMap = null;
		try {
			tableConfigMap = loadTable(bean, cluster,databaseSet);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		SchemaConfig schemaConfig=new SchemaConfig(cluster.getName(),null,tableConfigMap,5,false);
		Map<String,String> dataNodeDbTypeMap = new HashMap<>();
		String[] groupNameS=groupName.split(",");
		for(int i=0;i<groupNameS.length;i++) {
			dataNodeDbTypeMap.put(groupNameS[i], "mysql");
		}
		schemaConfig.setDataNodeDbTypeMap(dataNodeDbTypeMap);
		return schemaConfig;
	}

	public Client createLoginedClient() {
		ClientConfig conf = new ClientConfig();
		conf.register(JacksonJaxbJsonProvider.class);
		Client client = ClientBuilder.newClient(conf);
		appendLoginToken(client);
		return client;
	}

	public void appendLoginToken(Client client){
		WebTarget webTarget= client.target(MANAGER_ROOT_URI +"/login");
		Response response = webTarget.queryParam("username", "ROOT")
				.queryParam("password", "ea2adde5c377cb5e09d14b71935c6f32")
				.request(MediaType.APPLICATION_JSON_TYPE).get();
		if (response.getStatus() == 200){
			GenericType<Entity<String>> _type = new GenericType<Entity<String>>() {};
			Entity<String> entity = response.readEntity(_type);
			if (entity.getResCode()==10000){
				NewCookie sessionObject = response.getCookies().get(MANAGER_TOKEN_COOKIE);
				if (null != sessionObject) {
					client.property(TOKEN_PROP, sessionObject.getValue());
				}
			}
		}

	}

	/****
	 * 通过集群中的组获取组信息并封装
	 * @param cluster
	 */
	private String getGroupInfo(JsonToBean bean,Cluster cluster) {
		List<Group> groupList=cluster.getGroupList();
		String groupName="";
		if(groupList.size()>0) {
			for (int i = 0; i < groupList.size();i++){
				if(groupList.get(i).getName()!=null) {
					groupName+= groupList.get(i).getName();
					if (i < groupList.size() - 1) {
						groupName += ",";
					}
				}
				Group group=bean.getGroup(groupList.get(i).getId());
				if(group.getNodes().size()==0) {
				//没有节点的组
				DataNodeConfig dataNode = new DataNodeConfig(group.getName(),null,null);
				LOGGER.info("Group named "+dataNode.getName()+" does not exist node.");
				}else{
				//获取组下面的节点列表
				List<Node> nodeList=group.getNodes();
				DBHostConfig[] dbHostConfigs = new DBHostConfig[1];
				Map<Integer, DBHostConfig[]> readHostsMap = new HashMap<Integer, DBHostConfig[]>(2);
				List<Node> readNode=new ArrayList<>();
				for(int m=0;m<nodeList.size();m++){
				//通过id查询节点信息
				Node node=nodeList.get(m);
				if (node!=null) {
					if (node.getIsMaster() == 0) {
						readNode.add(node);
					} else {
						//获取writeHost
						dbHostConfigs[0] = getWriteHost((i + 1), node, node.getIsMaster(), 1);
					}
					if (m < nodeList.size() - 1) {
						continue;
					}
					if(readNode.size()>0) {
						DBHostConfig[] dbHostConfig = new DBHostConfig[readNode.size()];
						for (int k = 0; k < readNode.size(); k++) {
							dbHostConfig[k] = getWriteHost(k, node, node.getIsMaster(), readNode.size());
						}
						readHostsMap.put(group.getId(), dbHostConfig);
					}
						DataHostConfig hostConfig = new DataHostConfig("X" , "mysql", "native", dbHostConfigs, readHostsMap, 1,
																													 100, node.getIsMaster() == 1 ? true : false
						);
						hostConfig.setHearbeatSQL("select user()");
						//hostConfig
						dataHosts.put(hostConfig.getName(), hostConfig);
						DataNodeConfig dataNode = new DataNodeConfig(group.getName(), node.getSchema(), hostConfig.getName());
						dataNodes.put(group.getName(), dataNode);
					}
					}
				}
			}
		}
		return groupName;
	}

	/***
	 * 创建WriteHost
	 */
	private DBHostConfig getWriteHost(int i,Node node,Integer master,Integer nodeList) {
		DBHostConfig[] writeDbConfs =null;
		String hostName;
		if(master==1){
			writeDbConfs= new DBHostConfig[1];
			hostName="hostM"+i;
		}else {
			writeDbConfs= new DBHostConfig[nodeList];
			hostName = "hostS" + i;
		}
		DBHostConfig dbConfs=new DBHostConfig(hostName,node.getIp(),node.getPort(),node.getIp()+":"+node.getPort(),
																	node.getDbUsername(),node.getDbPassword(),null);
		dbConfs.setMaxCon(10);
		dbConfs.setMinCon(5);
		if(master==1){
			writeDbConfs[0]=dbConfs;
			return writeDbConfs[0];
		}else {
			writeDbConfs[i] = dbConfs;
			return writeDbConfs[i];
		}
	}

	@Override
	public Map<String, SchemaConfig> getSchemaConfigs() {
		Client client = createLoginedClient();
		JsonToBean bean=new JsonToBean(client);
		List<Cluster> clusterList=bean.getClientList(MANAGER_ROOT_URI +"/cluster");
		for(Cluster cluster:clusterList) {
			SchemaConfig schemasConfig = getSchemaConfig(MANAGER_ROOT_URI + "/cluster/cluster/"+cluster.getId());
			if(schemasConfig!=null) {
				schemas.put(schemasConfig.getName(), schemasConfig);
			}
		}
		return schemas;
	}

		private Map<String, TableConfig> loadTable(JsonToBean bean,Cluster cluster,Set<String> dbSet)throws ClassNotFoundException,
																		InstantiationException, IllegalAccessException,
																		InvocationTargetException {
		String groupNames = getGroupNames(cluster);
		String[] groupName=groupNames.split(",");
		Map<String, TableConfig> tableName2TableConfigMapper = new HashMap<>();
		TableRuleConfig tableRuleConfig=null;
		RuleConfig ruleConfig=null;
		//通过db获取其下的所有表信息
		Map<String,List<String>> tables=bean.getTableInfo(dbSet);
		//通过db的名字加表名获取规则信息
		for(String dbName:tables.keySet()) {
			for(int i=0;i<tables.get(dbName).size();i++) {
				String tableName=tables.get(dbName).get(i);
				List<Rule> rule=bean.getRuleInfo(dbName, tableName);
				if(rule.size()!=0){
					for(int j=0;j<rule.size();j++){
						String fullColumnName=rule.get(j).getFullColumnName();
						String functionName=rule.get(j).getRuleType();
						String column=fullColumnName.substring(fullColumnName.lastIndexOf(".")+1);
						SplitType splitType=null;
						if("HASH".equals(functionName)){
							functionName="jump-consistent-hash";
							splitType=SplitType.HASH;
						}
						ruleConfig=new RuleConfig(column.toUpperCase(),functionName);
						ruleConfig=getRuleConfig(ruleConfig,splitType,groupName.length);
						String newName=rule.get(j).getName()+"_"+tableName;
						tableRuleConfig=new TableRuleConfig(newName,ruleConfig);
						tableRules.put(newName,tableRuleConfig);
					}
				}
				//封装
				TableConfig table = new TableConfig(tableName, null,
																						false, true, TableConfig.TYPE_GLOBAL_DEFAULT, groupNames,
																						getDbType(groupNames),
																						(tableRuleConfig != null) ? tableRuleConfig.getRule() : null,
																						false, null, false, null, null,"");
				tableName2TableConfigMapper.put(table.getName(), table);
			}
		}

		return tableName2TableConfigMapper;

	}

	private RuleConfig getRuleConfig(RuleConfig ruleConfig, SplitType splitType,Integer count){
		Class<? extends AbstractPartitionAlgorithm> partitionAlg = SplitType2Algorithm.getBySplitType(splitType);
		if(partitionAlg == null){
			throw new RuntimeException("table name: [" + ruleConfig + "] has unsupport partition algorithm, please check!");
		}
		try {
			AbstractPartitionAlgorithm partitionAlgorithm = partitionAlg.newInstance();
			Map<String,Integer> totalBuckets=new HashMap<>();
			totalBuckets.put("count",count);
			try {
				ParameterMapping.mapping(partitionAlgorithm, totalBuckets);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			partitionAlgorithm.init();
			ruleConfig.setRuleAlgorithm(partitionAlgorithm);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return ruleConfig;
	}

	private String getGroupNames(Cluster cluster){
		StringBuilder builder = new StringBuilder();
		List<Group> groupList = cluster.getGroupList();
		for (int i = 0; i < groupList.size();i++) {
			if (groupList.get(i).getName() != null) {
				builder.append(groupList.get(i).getName());
				if (i < groupList.size() - 1) {
					builder.append(",");
				}
			}
		}
		return builder.toString();
	}

	@Override
	public Map<String, DataNodeConfig> getDataNodes() {
		// TODO Auto-generated method stub
		return dataNodes;
	}

	@Override
	public Map<String, DataHostConfig> getDataHosts() {
		// TODO Auto-generated method stub
		return dataHosts;
	}

	@Override
	public SystemConfig getSystemConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserConfig getUserConfig(String user) {

		return null;
	}

	@Override
	public Map<String, UserConfig> getUserConfigs() {
		Map<String, UserConfig> userConfig=new HashMap<>();
		//查询集群列表
		String url=MANAGER_ROOT_URI+"/cluster";
		Client client = createLoginedClient();
		JsonToBean bean=new JsonToBean(client);
		List<Cluster> clusterList=bean.getClientList(url);
		if(clusterList.size()==0){
			LOGGER.error("No cluster list information or cluster list was empty.");
			return null;
		}
		//封装所有的集群到userConfig中
		String clusterName="";
		//获取用户列表
		url=MANAGER_ROOT_URI+"/user";
		List<User> users=bean.getUserList(url);
		for (User user: users) {
			UserConfig userCon=new UserConfig();
			userCon.setName(user.getUsername());
			userCon.setPassword(user.getPassword());
			userCon.setEncryptPassword(user.getPassword());
			for(int i=0;i<clusterList.size();i++){
				String name=clusterList.get(i).getName();
				for(String config:schemas.keySet()){
					if(name.equals(config)){
						clusterName+=clusterList.get(i).getName();
						if(i<clusterList.size()){
							clusterName+=",";
						}
					}
				}
			}
			String[] cluster=clusterName.split(",");
			userCon.setSchemas(new HashSet<String>(Arrays.asList(cluster)));
			loadPrivileges(userCon);
			userConfig.put(userCon.getName(),userCon);
		}
		return userConfig;
	}

	/***
	 * 封装SQL表级的增删改查权限控制
	 * @param user
	 */
	private void loadPrivileges(UserConfig user) {
		UserPrivilegesConfig privilegesConfig = new UserPrivilegesConfig();
		//TODO 根据后续要求进行封装
		user.setPrivilegesConfig(privilegesConfig);
	}

	@Override
	public FirewallConfig getFirewallConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClusterConfig getClusterConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	private Set<String> getDbType(String dataNode) {
		Set<String> dbTypes = new HashSet<>();
		String[] dataNodeArr = SplitUtil.split(dataNode, ',', '$', '-');
		for (String node : dataNodeArr) {
			DataNodeConfig datanode = dataNodes.get(node);
			DataHostConfig datahost = dataHosts.get(datanode.getDataHost());
			dbTypes.add(datahost.getDbType());
		}
		return dbTypes;
	}

}
