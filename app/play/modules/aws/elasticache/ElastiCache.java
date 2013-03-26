package play.modules.aws.elasticache;

import net.spy.memcached.MemcachedClient;
import play.api.cache.CacheAPI;
import scala.Option;

public class ElastiCache implements CacheAPI {
	
	private final MemcachedClient client;
 
	public ElastiCache(MemcachedClient client) {
		this.client = client;
	}
	
	@Override
	public Option<Object> get(String key) {
		return Option.apply(client.get(key));
	}

	@Override
	public void set(String key, Object value, int expiration) {
		client.set(key, expiration, value);
	}
	
	public void remove(String key) {
		client.delete(key);
	}

}
