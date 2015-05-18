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

public enum ParsingEvent {
    START_OBJECT,
    START_DOUBLE_QUOTE_STRING,
    START_SINGLE_QUOTE_STRING,
    START_INLINE_NUMERIC_VALUE,
    END_INLINE_NUMERIC_VALUE,
    END_DOUBLE_QUOTE_STRING,
    END_SINGLE_QUOTE_STRING,
    KEY_VALUE_DELIMITER,
    PARAMETER,
    AND_SEPARATOR,
    IN_ARRAY_AND_SEPARATOR, KEY_START, KEY_END, END_OF_IN_ARRAY_OBJECT

}
