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

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMapper;
import play.Application;
import play.Play;

public class DynamoDB {

    public static DynamoDBMapper mapper() {
        Application app = Play.application();
        if (app == null) {
            throw new RuntimeException("No application running");
        }
        DynamoDBPlugin plugin = app.plugin(DynamoDBPlugin.class);
        DynamoDBMapper mapper = plugin.mapper();
        if (mapper == null) {
            throw new RuntimeException("No DynamoDBMapper configured");
        }
        return mapper;
    }

}
