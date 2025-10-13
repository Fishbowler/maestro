/*
 *
 *  Copyright (c) 2022 mobile.dev inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package maestro.orchestra.yaml

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * Represents a boolean value that can be either a literal boolean or a string expression
 * that will be evaluated at runtime (e.g., "${1 < 2}" or "${someVar}").
 */
@JsonDeserialize(using = YamlScriptableBooleanDeserializer::class)
sealed class YamlScriptableBoolean {
    data class Literal(val value: Boolean) : YamlScriptableBoolean()
    data class StringExpression(val expression: String) : YamlScriptableBoolean()

    fun toStringValue(): String {
        return when (this) {
            is Literal -> value.toString()
            is StringExpression -> expression
        }
    }
}

class YamlScriptableBooleanDeserializer : JsonDeserializer<YamlScriptableBoolean>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): YamlScriptableBoolean {
        val node: JsonNode = p.codec.readTree(p)

        return when {
            node.isBoolean -> YamlScriptableBoolean.Literal(node.asBoolean())
            node.isTextual -> YamlScriptableBoolean.StringExpression(node.asText())
            else -> throw IllegalArgumentException("Expected boolean or string value, got: $node")
        }
    }
}
