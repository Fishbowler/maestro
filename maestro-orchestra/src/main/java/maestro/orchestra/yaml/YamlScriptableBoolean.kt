package maestro.orchestra.yaml

import com.fasterxml.jackson.annotation.JsonCreator
import com.google.protobuf.value

data class YamlScriptableBoolean(
    val value: Boolean? = null,
    val script: String? = null,
){
    companion object {

        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        fun parse(input: Any): YamlScriptableBoolean {
            return when (input) {
                is String -> YamlScriptableBoolean(script = input)
                is Boolean -> YamlScriptableBoolean(value = input)
                else -> YamlScriptableBoolean(value = null, script = null)
            }
        }
    }
}
