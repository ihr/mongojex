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


import org.apache.commons.lang3.math.NumberUtils;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static org.mongojx.fluent.core.ParsingEvent.*;

public class MongoJxParser {


    public static QueryTuple bind(String filter, Object... parameters) {
        ParsingEvent state = null;
        int keyStartIndex = 0;
        int keyStopIndex = 0;
        int valueStartIndex = 0;
        int parameterIndex = 0;
        Document finalDocument = new Document();
        Document document = finalDocument;
        int depth = 0;
        int arrays = 1;
        List<Document> documents = new ArrayList();

        for (int i = 0; i < filter.length(); i++) {
            char character = filter.charAt(i);
            if (isJsonObjectStarting(character)) {
                if (state == KEY_VALUE_DELIMITER) {
                    Document nestedDocument = new Document();
                    document.put(filter.substring(keyStartIndex, keyStopIndex), nestedDocument);
                    document = nestedDocument;
                } else if (state == IN_ARRAY_AND_SEPARATOR) {
                    document = new Document();
                    finalDocument = document;
                }
                state = START_OBJECT;
                keyStartIndex = i + 1;
                depth++;
            } else if (isDoubleStringCharacter(character)) {
                if (isDoubleStringEnding(state)) {
                    state = END_DOUBLE_QUOTE_STRING;
                    document.put(filter.substring(keyStartIndex, keyStopIndex), filter.substring(valueStartIndex, i));
                } else {
                    valueStartIndex = i + 1;
                    state = START_DOUBLE_QUOTE_STRING;
                }
            } else if (isSingleStringCharacter(character)) {
                if (isSingleStringEnding(state)) {
                    state = END_SINGLE_QUOTE_STRING;
                } else if (isSingleStringStarting(state)) {
                    keyStartIndex = i;
                    state = START_SINGLE_QUOTE_STRING;
                }
            } else if (isJsonObjectEnding(character)) {
                depth--;
                if (depth == 0) {
                    documents.add(finalDocument);
                    state = END_OF_IN_ARRAY_OBJECT;
                }
            } else if (isArrayStarting(character)) {
                arrays--;
                if (arrays == 0) {
                    return new QueryTuple(i + 1, documents);
                }
            } else if (isArrayEnding(character)) {
                QueryTuple queryTuple = bind(filter.substring(i + 1), parameters);
                document.put(filter.substring(keyStartIndex, keyStopIndex), queryTuple.getDocuments());
                i = i + queryTuple.getIndex();
            } else if (isKeyValueSeparator(character)) {
                if (state != KEY_END) {
                    keyStopIndex = i;
                }
                document.put(filter.substring(keyStartIndex, keyStopIndex), "");
                state = KEY_VALUE_DELIMITER;
            } else if (isParameter(character)) {
                state = PARAMETER;
                document.put(filter.substring(keyStartIndex, keyStopIndex), parameters[parameterIndex]);
                parameterIndex++;
            } else if (isSeparator(character)) {
                keyStartIndex = i + 1;
                if (state == END_OF_IN_ARRAY_OBJECT) {
                    state = IN_ARRAY_AND_SEPARATOR;
                } else {
                    state = AND_SEPARATOR;
                }
            } else if (Character.isWhitespace(character)) {
                if (state == AND_SEPARATOR) {
                    keyStartIndex = i + 1;
                } else if (state == START_OBJECT) {
                    keyStartIndex = i + 1;
                } else if (state == START_INLINE_NUMERIC_VALUE) {
                    state = END_INLINE_NUMERIC_VALUE;
                    String value = filter.substring(valueStartIndex, i);
                    document.put(filter.substring(keyStartIndex, keyStopIndex), NumberUtils.createNumber(value));
                } else if (state == KEY_START) {
                    keyStopIndex = i;
                    state = KEY_END;
                }
            } else if (Character.isDigit(character) && state == KEY_VALUE_DELIMITER) {
                state = START_INLINE_NUMERIC_VALUE;
                valueStartIndex = i;
            } else if (Character.isAlphabetic(character) && (state == AND_SEPARATOR || state == START_OBJECT)) {
                state = KEY_START;
            }

        }

        return new QueryTuple(filter.length(), documents);
    }

    private static boolean isSeparator(char character) {
        return character == ',';
    }

    private static boolean isParameter(char character) {
        return character == '#';
    }

    private static boolean isKeyValueSeparator(char character) {
        return character == ':';
    }

    private static boolean isArrayEnding(char character) {
        return character == '[';
    }

    private static boolean isArrayStarting(char character) {
        return character == ']';
    }

    private static boolean isDoubleStringEnding(ParsingEvent state) {
        return state == START_DOUBLE_QUOTE_STRING;
    }

    private static boolean isSingleStringStarting(ParsingEvent state) {
        return state != START_DOUBLE_QUOTE_STRING;
    }

    private static boolean isSingleStringEnding(ParsingEvent state) {
        return state == START_SINGLE_QUOTE_STRING;
    }

    private static boolean isSingleStringCharacter(char character) {
        return character == '\'';
    }

    private static boolean isDoubleStringCharacter(char character) {
        return character == '\"';
    }

    private static boolean isJsonObjectStarting(char character) {
        return character == '{';
    }

    private static boolean isJsonObjectEnding(char character) {
        return character == '}';
    }
}
