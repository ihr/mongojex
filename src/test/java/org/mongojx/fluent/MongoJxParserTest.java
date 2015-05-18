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

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.mongojx.fluent.core.MongoJxParser;

import java.io.IOException;

public class MongoJxParserTest {

    @Test
    public void should_bind_single_parameter() throws IOException {
        //GIVEN

        //WHEN
        Document bind = MongoJxParser.bind("{_id: #}", new ObjectId("551d6a519868833a7ede64fe")).getDocuments().get(0);

        //THEN
        Assertions.assertThat(bind.toJson()).isEqualTo("{ \"_id\" : { \"$oid\" : \"551d6a519868833a7ede64fe\" } }");
    }

    @Test
    public void should_bind_array_of_parameters() throws IOException {
        //GIVEN

        //WHEN
        Document bind = MongoJxParser.bind("{username: {$in: # }}", new String[]{"producer", "user"}).getDocuments().get(0);

        //THEN
        Assertions.assertThat(bind.toJson()).isEqualTo("{ \"username\" : { \"$in\" : \"producer\" } }");
    }

    @Test
    public void should_bind_list_of_parameters() throws IOException {
        //GIVEN

        //WHEN
        Document bind = MongoJxParser.bind("{username: {$in: # }}", Lists.newArrayList("producer", "user")).getDocuments().get(0);

        //THEN
        Assertions.assertThat(bind.toJson()).isEqualTo("{ \"username\" : { \"$in\" : [\"producer\", \"user\"] } }");
    }

    @Test
    public void should_bind_or_parameters() throws IOException {
        //GIVEN

        //WHEN
        Document bind = MongoJxParser.bind("{name: \"ingini\", 45degree: #, $or: [ { quantity: { $lt: 20 } }, { price: 10 } ] }", 45.03).getDocuments().get(0);

        //THEN
        Assertions.assertThat(bind.toJson()).isEqualTo("{ \"name\" : \"ingini\", \"45degree\" : 45.03, \"$or\" : [{ \"quantity\" : { \"$lt\" : 20 } }, { \"price\" : 10 }] }");
    }

    @Test
    public void should_bind_and_or_parameters() throws IOException {
        //GIVEN

        //WHEN
        Document bind = MongoJxParser.bind("{name: \"ingini\", $and : [ {$or: [ { quantity: { $lt: 20 } }, { price: 10 } ]}, {45degree: #}] }", 45.03).getDocuments().get(0);

        //THEN
        Assertions.assertThat(bind.toJson()).isEqualTo("{ \"name\" : \"ingini\", \"$and\" : [{ \"$or\" : [{ \"quantity\" : { \"$lt\" : 20 } }, { \"price\" : 10 }] }, { \"45degree\" : 45.03 }] }");
    }

    @Test
    public void should_bind_simple_with_space_parameter() throws IOException {
        //GIVEN

        //WHEN
        Document bind = MongoJxParser.bind("{name: \"John Doe\"}").getDocuments().get(0);

        //THEN
        Assertions.assertThat(bind.toJson()).isEqualTo("{ \"name\" : \"John Doe\" }");
    }

    @Test
    public void should_bind_simple_with_apostrophe_parameters() throws IOException {
        //GIVEN

        //WHEN
        Document bind = MongoJxParser.bind("{subject: \"Mc O'Neil it's cool\"}").getDocuments().get(0);

        //THEN
        Assertions.assertThat(bind.toJson()).isEqualTo("{ \"subject\" : \"Mc O'Neil it's cool\" }");
    }

}
