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

package maestro.orchestra

import maestro.js.JsEngine
import maestro.orchestra.util.Env.evaluateScripts

data class ElementSelector(
    val textRegex: String? = null,
    val idRegex: String? = null,
    val size: SizeSelector? = null,
    val below: ElementSelector? = null,
    val above: ElementSelector? = null,
    val leftOf: ElementSelector? = null,
    val rightOf: ElementSelector? = null,
    val containsChild: ElementSelector? = null,
    val containsDescendants: List<ElementSelector>? = null,
    val traits: List<ElementTrait>? = null,
    val index: String? = null,
    val enabled: String? = null, // String to allow script evaluation, converted to Boolean later
    @Deprecated("This is a deprecated field, please use the optional in commands interface")
    val optional: Boolean = false,
    val selected: String? = null, // String to allow script evaluation, converted to Boolean later
    val checked: String? = null, // String to allow script evaluation, converted to Boolean later
    val focused: String? = null, // String to allow script evaluation, converted to Boolean later
    val childOf: ElementSelector? = null,
    val css: String? = null,
) {

    data class SizeSelector(
        val width: Int? = null,
        val height: Int? = null,
        val tolerance: Int? = null,
    )

    fun evaluateScripts(jsEngine: JsEngine): ElementSelector {
        return copy(
            textRegex = textRegex?.evaluateScripts(jsEngine),
            idRegex = idRegex?.evaluateScripts(jsEngine),
            below = below?.evaluateScripts(jsEngine),
            above = above?.evaluateScripts(jsEngine),
            leftOf = leftOf?.evaluateScripts(jsEngine),
            rightOf = rightOf?.evaluateScripts(jsEngine),
            containsChild = containsChild?.evaluateScripts(jsEngine),
            containsDescendants = containsDescendants?.map { it.evaluateScripts(jsEngine) },
            index = index?.evaluateScripts(jsEngine),
            enabled = enabled?.evaluateScripts(jsEngine),
            selected = selected?.evaluateScripts(jsEngine),
            checked = checked?.evaluateScripts(jsEngine),
            focused = focused?.evaluateScripts(jsEngine),
            childOf = childOf?.evaluateScripts(jsEngine),
            css = css?.evaluateScripts(jsEngine),
        )
    }

    fun description(): String {
        val descriptions = mutableListOf<String>()

        textRegex?.let {
            descriptions.add("\"$it\"")
        }

        idRegex?.let {
            descriptions.add("id: $it")
        }

        enabled?.let {
            val boolValue = it.toBooleanStrictOrNull()
            when(boolValue){
                true -> descriptions.add("enabled")
                false -> descriptions.add("disabled")
                null -> descriptions.add("enabled: $it")
            }
        }

        below?.let {
            descriptions.add("Below ${it.description()}")
        }

        above?.let {
            descriptions.add("Above ${it.description()}")
        }

        leftOf?.let {
            descriptions.add("Left of ${it.description()}")
        }

        rightOf?.let {
            descriptions.add("Right of ${it.description()}")
        }

        containsChild?.let {
            descriptions.add("Contains child: ${it.description()}")
        }

        containsDescendants?.let { selectors ->
            val descendantDescriptions = selectors.joinToString(", ") { it.description() }
            descriptions.add("Contains descendants: [$descendantDescriptions]")
        }

        size?.let {
            var description = "Size: ${it.width}x${it.height}"
            it.tolerance?.let { tolerance ->
                description += "(tolerance: $tolerance)"
            }

            descriptions.add(description)
        }

        traits?.let {
            descriptions.add(
                "Has traits: ${traits.joinToString(", ") { it.description }}"
            )
        }

        index?.let {
            descriptions.add("Index: ${it.toDoubleOrNull()?.toInt() ?: it}")
        }

        selected?.let {
            val boolValue = it.toBooleanStrictOrNull()
            when(boolValue){
                true -> descriptions.add("selected")
                false -> descriptions.add("not selected")
                null -> descriptions.add("selected: $it")
            }
        }

        checked?.let {
            val boolValue = it.toBooleanStrictOrNull()
            when(boolValue){
                true -> descriptions.add("checked")
                false -> descriptions.add("not checked")
                null -> descriptions.add("checked: $it")
            }
        }

        focused?.let {
            val boolValue = it.toBooleanStrictOrNull()
            when(boolValue){
                true -> descriptions.add("focused")
                false -> descriptions.add("not focused")
                null -> descriptions.add("focused: $it")
            }
        }

        childOf?.let {
            descriptions.add("Child of: ${it.description()}")
        }

        css?.let {
            descriptions.add("CSS: $it")
        }

        return descriptions.joinToString(", ")
    }

}
