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

package play.modules.aws;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DynamoDBModel {

    private Method hashAccessor;

    private Method getHashAccessor() {
        if (hashAccessor == null) {
            Class<?> clazz = this.getClass();
            while (clazz != null) {
                for (Method m : clazz.getDeclaredMethods()) {
                    if (m.isAnnotationPresent(com.amazonaws.services.dynamodb.datamodeling.DynamoDBHashKey.class)) {
                        hashAccessor = m;
                    }
                }
                clazz = clazz.getSuperclass();
            }
            if (hashAccessor == null) {
                throw new RuntimeException("No method annotated with @com.amazonaws.services.dynamodb.datamodeling.DynamoDBHashKey found in class [" + this.getClass() + "]");
            }
        }
        return hashAccessor;
    }

    public void save() {
        DynamoDB.mapper().save(this);
    }

    public void delete() {
        DynamoDB.mapper().delete(this);
    }

    public void refresh() {
        try {
            DynamoDB.mapper().load(this.getClass(), getHashAccessor().invoke(this));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
