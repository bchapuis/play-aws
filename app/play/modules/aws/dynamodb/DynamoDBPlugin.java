/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package play.modules.aws.dynamodb;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMapper;
import play.Application;
import play.Configuration;
import play.Logger;
import play.Plugin;


public class DynamoDBPlugin extends Plugin {
    
	private final Application application;

    private AmazonDynamoDBClient client;
    
    private DynamoDBMapper mapper;

    public DynamoDBPlugin(Application application) {
        this.application = application;
    }

    @Override
    public void onStart() {
        Configuration aws = Configuration.root().getConfig("aws");
        Configuration dynamodb = Configuration.root().getConfig("dynamodb");
        if (aws != null && dynamodb != null) {
            String accesskey = aws.getString("accesskey");
            String secretkey = aws.getString("secretkey");
            String endpoint = dynamodb.getString("endpoint");
            if (accesskey != null && secretkey != null && endpoint != null) {
                AWSCredentials credentials = new BasicAWSCredentials(accesskey, secretkey);
                client = new AmazonDynamoDBClient(credentials);
                client.setEndpoint(endpoint);
                mapper = new DynamoDBMapper(client);
            }
        }
        Logger.info("DynamoDBPlugin has started");
    }

    @Override
    public void onStop() {
        Logger.info("DynamoDBPlugin has stopped");
    }
    
    public AmazonDynamoDBClient client() {
        return client;
    }

    public DynamoDBMapper mapper() {
        return mapper;
    }
}