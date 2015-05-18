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

package org.mongojx.fluent.core;

import org.bson.Document;

import java.util.List;

public class QueryTuple {

    private final Integer index;
    private final List<Document> documents;

    public QueryTuple(Integer index, List<Document> documents) {
        this.index = index;
        this.documents = documents;
    }

    public Integer getIndex() {
        return index;
    }

    public List<Document> getDocuments() {
        return documents;
    }
}
