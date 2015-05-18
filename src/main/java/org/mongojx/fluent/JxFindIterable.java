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
import com.mongodb.CursorType;
import com.mongodb.Function;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.mongojx.fluent.core.MongoJxParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JxFindIterable {

    private final ObjectMapper mapper;
    private FindIterable<Document> findIterable;

    public JxFindIterable(ObjectMapper mapper, MongoCollection collection, String filter, Object... parameters) {
        this.mapper = mapper;
        findIterable = collection.find(MongoJxParser.bind(filter, parameters).getDocuments().get(0));
    }

    /**
     * Sets the limit to apply.
     *
     * @param limit the limit, which may be null
     * @return this
     * @see FindIterable#limit(int)
     */
    public JxFindIterable limit(int limit) {
        findIterable = findIterable.limit(limit);
        return this;
    }

    /**
     * Sets the number of documents to skip.
     *
     * @param skip the number of documents to skip
     * @return this
     * @see FindIterable#skip(int)
     */
    public JxFindIterable skip(int skip) {
        findIterable = findIterable.skip(skip);
        return this;
    }

    /**
     * Sets the maximum execution time on the server for this operation.
     *
     * @param maxTime  the max time
     * @param timeUnit the time unit, which may not be null
     * @return this
     * @see FindIterable#maxTime(long, TimeUnit)
     */
    public JxFindIterable maxTime(long maxTime, TimeUnit timeUnit) {
        findIterable = findIterable.maxTime(maxTime, timeUnit);
        return this;
    }

    /**
     * Sets the query modifiers to apply to this operation.
     *
     * @param modifiers the query modifiers to apply, which may be null.
     * @return this
     * @see FindIterable#modifiers(Bson)
     */
    public JxFindIterable modifiers(String modifiers) {
        findIterable.modifiers(Document.parse(modifiers));
        return this;
    }

    /**
     * Sets a document describing the fields to return for all matching documents.
     *
     * @param projection the project document, which may be null.
     * @return this
     * @see FindIterable#projection(Bson)
     */
    public JxFindIterable projection(String projection) {
        findIterable = findIterable.projection(Document.parse(projection));
        return this;
    }

    /**
     * Sets the sort criteria to apply to the query.
     *
     * @param sort the sort criteria, which may be null.
     * @return this
     * @see FindIterable#sort(Bson)
     */
    public JxFindIterable sort(Bson sort) {
        findIterable = findIterable.sort(sort);
        return this;
    }

    /**
     * The server normally times out idle cursors after an inactivity period (10 minutes)
     * to prevent excess memory use. Set this option to prevent that.
     *
     * @param noCursorTimeout true if cursor timeout is disabled
     * @return this
     * @see FindIterable#noCursorTimeout(boolean)
     */
    public JxFindIterable noCursorTimeout(boolean noCursorTimeout) {
        findIterable = findIterable.noCursorTimeout(noCursorTimeout);
        return this;
    }

    /**
     * Users should not set this under normal circumstances.
     *
     * @param oplogReplay if oplog replay is enabled
     * @return this
     * @see FindIterable#oplogReplay(boolean)
     */
    public JxFindIterable oplogReplay(boolean oplogReplay) {
        findIterable = findIterable.oplogReplay(oplogReplay);
        return this;
    }

    /**
     * Get partial results from a sharded cluster if one or more shards are unreachable (instead of throwing an error).
     *
     * @param partial if partial results for sharded clusters is enabled
     * @return this
     * @see FindIterable#partial(boolean)
     */
    public JxFindIterable partial(boolean partial) {
        findIterable = findIterable.partial(partial);
        return this;
    }

    /**
     * Sets the cursor type.
     *
     * @param cursorType the cursor type
     * @return this
     * @see FindIterable#cursorType(CursorType)
     */
    public JxFindIterable cursorType(CursorType cursorType) {
        findIterable = findIterable.cursorType(cursorType);
        return this;
    }

    /**
     * Sets the number of documents to return per batch.
     *
     * @param batchSize the batch size
     * @return this
     * @see FindIterable#batchSize(int)
     */
    public JxFindIterable batchSize(int batchSize) {
        findIterable = findIterable.batchSize(batchSize);
        return this;
    }

    public <U> MongoIterable<U> mapAs(Function<Document, U> mapper) {
        return findIterable.map(mapper);
    }

    public <R> List<R> asList(final Class<R> target) {

        return findIterable.map(result -> {
            try {
                return mapper.readValue(result.toJson(), target);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }).into(new ArrayList<R>());
    }
}
