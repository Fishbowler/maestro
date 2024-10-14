package maestro.orchestra

import maestro.js.JsEngine
import maestro.orchestra.util.Env.evaluateScripts

// Note: The appId config is only a yaml concept for now. It'll be a larger migration to get to a point
// where appId is part of MaestroConfig (and factored out of MaestroCommands - eg: LaunchAppCommand).
data class MaestroConfig(
    val appId: String? = null,
    val name: String? = null,
    val tags: List<String>? = emptyList(),
    val ext: Map<String, Any?> = emptyMap(),
    val onFlowStart: MaestroOnFlowStart? = null,
    val onFlowComplete: MaestroOnFlowComplete? = null,
) {

    fun evaluateScripts(jsEngine: JsEngine): MaestroConfig {
        return copy(
            appId = appId?.evaluateScripts(jsEngine),
            name = name?.evaluateScripts(jsEngine),
            onFlowComplete = onFlowComplete?.evaluateScripts(jsEngine),
            onFlowStart = onFlowStart?.evaluateScripts(jsEngine),
        )
    }

    fun applyWorkspaceDefaults(workspaceConfig: WorkspaceConfig): MaestroConfig {
        val potentialFlowDefaults = workspaceConfig.potentialFlowDefaults
        return copy(
            appId = (appId ?: (potentialFlowDefaults["appId"] as String).firstOrNull()).toString(),
            ext = ext.mapValues {
                if(it.key == "jsEngine") {
                    it.value ?: (potentialFlowDefaults[it.key] as String).firstOrNull()
                } else {
                    it.value
                }
            }
        )
    }
}

data class MaestroOnFlowComplete(val commands: List<MaestroCommand>) {
    fun evaluateScripts(jsEngine: JsEngine): MaestroOnFlowComplete {
        return this
    }
}

data class MaestroOnFlowStart(val commands: List<MaestroCommand>) {
    fun evaluateScripts(jsEngine: JsEngine): MaestroOnFlowStart {
        return this
    }
}
