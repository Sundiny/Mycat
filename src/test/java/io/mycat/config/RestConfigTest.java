package io.mycat.config;

import static io.mycat.config.loader.rest.RestConfigLoader.*;

import io.mycat.config.loader.rest.RestConfigLoader;
import io.mycat.config.model.SchemaConfig;
import io.mycat.config.model.entity.Cluster;
import io.mycat.config.util.JsonToBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;

public class RestConfigTest {
	private Client client;
	public Client createLoginedClient() {
		Client client = ClientBuilder.newClient();
		appendLoginToken(client);
		return client;
	}
	private  void appendLoginToken(Client c){
		WebTarget webTarget= createLoginedClient().target(MANAGER_ROOT_URI);
		MultivaluedMap<String, String> formData = new MultivaluedHashMap<String, String>();
		formData.add("username", "ROOT");
		formData.add("password", "ea2adde5c377cb5e09d14b71935c6f32");
		Response response = webTarget.request().post(Entity.form(formData));
		if (response.getStatus() == 200){
			//get session id
			GenericType<io.mycat.config.model.manager.Entity<Cluster>> _type = new GenericType <io.mycat.config.model.manager.Entity<Cluster>>(){};
			io.mycat.config.model.manager.Entity<Cluster> cluster = response.readEntity(_type);
			if (cluster.getResCode() == 10000){
				NewCookie sessionObject = response.getCookies().get(MANAGER_TOKEN_COOKIE);
				if (null != sessionObject)
					c.property(TOKEN_PROP, sessionObject.getValue());
			}
		}

	}
	@Before
	public void setUp() throws Exception {
		client = createLoginedClient();
	}

	@After
	public void tearDown() throws Exception {
	}

/*
	@Test
	public void testSchema() {
		RestConfigLoader restLoader=new RestConfigLoader();
		SchemaConfig schema=restLoader.getSchemaConfig("http://v24g.holytoolz.com:8080/epsm/cluster/cluster/5");
		System.out.println("测试通过");
		JsonToBean bean=new JsonToBean(client);
		bean.getClientList("http://v24g.holytoolz.com:8080/epsm/cluster");
		*/
/*ruleEntity ruleEntity=bean.getRuleByJson("http://v24g.holytoolz.com:8080/epsm/strategy");
		System.out.println(ruleEntity.getResMsg());
		System.out.println(ruleEntity.getData().get(4).toString());*//*


	}
*/

}
