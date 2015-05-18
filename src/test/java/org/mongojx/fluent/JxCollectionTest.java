/*
 *
 *  * Copyright (c) 2015 Ivan Hristov <hristov[DOT]iv[AT]gmail[DOT]com>
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * 	http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.mongojx.fluent;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Ignore;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.List;

public class JxCollectionTest {

    @Test
    @Ignore
    public void test() {

        MongoCollection mongoCollection = new MongoClient("localhost", 27017).getDatabase("test").getCollection("users");

        JxCollection jxCollection = new JxCollection(mongoCollection, new ObjectMapper());
        List<User> users = jxCollection.find("{_id: #}", new ObjectId("551d6a519868833a7ede64fe")).asList(User.class);
        System.out.println(users);

    }
}
