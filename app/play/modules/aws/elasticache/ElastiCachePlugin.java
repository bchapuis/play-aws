package play.modules.aws.elasticache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import play.Application;
import play.Configuration;
import play.Logger;
import play.api.cache.CacheAPI;
import play.api.cache.CachePlugin;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.elasticache.AmazonElastiCache;
import com.amazonaws.services.elasticache.AmazonElastiCacheClient;
import com.amazonaws.services.elasticache.model.CacheCluster;
import com.amazonaws.services.elasticache.model.CacheNode;
import com.amazonaws.services.elasticache.model.DescribeCacheClustersRequest;
import com.amazonaws.services.elasticache.model.DescribeCacheClustersResult;

public class ElastiCachePlugin extends CachePlugin {

	private final Application application;
	
	private AmazonElastiCache elasticacheClient;
	
	private MemcachedClient memcachedClient;
	
	public ElastiCachePlugin(Application application) {
        this.application = application;
    }
    
    @Override
    public void onStart() {
        Configuration aws = Configuration.root().getConfig("aws");
        Configuration elasticache = Configuration.root().getConfig("elasticache");
        if (aws != null && elasticache != null) {
            String accesskey = aws.getString("accesskey");
            String secretkey = aws.getString("secretkey");
            String clusterid = elasticache.getString("cluster");
            if (accesskey != null && secretkey != null && clusterid != null) {
            	// elasticache client
                AWSCredentials credentials = new BasicAWSCredentials(accesskey, secretkey);
                elasticacheClient = new AmazonElastiCacheClient(credentials);
                String endpoint = aws.getString("endpoint");
                if (endpoint != null) {
                    elasticacheClient.setEndpoint(endpoint);
                }
                
                // retrieve endpoints
                List<String> endpoints = new ArrayList<String>();
                DescribeCacheClustersRequest request = new DescribeCacheClustersRequest();
                request.setShowCacheNodeInfo(true);
                request.setCacheClusterId(clusterid);
                DescribeCacheClustersResult result = elasticacheClient.describeCacheClusters(request);
                List<CacheCluster> clusters = result.getCacheClusters();
                for (CacheCluster cluster : clusters) {
                    List<CacheNode> nodes = cluster.getCacheNodes();
                    for (CacheNode node : nodes) {
                    	endpoints.add(node.getEndpoint().getAddress());
                    }
                }
                
                // create the memcached client
                ConnectionFactoryBuilder connectionFactoryBuilder = new ConnectionFactoryBuilder();
                try {
					memcachedClient = new MemcachedClient(connectionFactoryBuilder.build(), AddrUtil.getAddresses(endpoints));
                } catch (IOException e) {
					
				}
            }
        }
        
        Logger.info("ElastiCachePlugin has started");
    }

    @Override
    public void onStop() {
        Logger.info("ElastiCachePlugin has stopped");
    }

	@Override
	public CacheAPI api() {
		return new Memcached(memcachedClient);
	}
	
	
}
