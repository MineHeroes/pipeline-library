static def getDefaultConfig() {
    return [
            javadoc        : true,
            source         : true,
            archive        : true,
            fixPOMVersion  : true,
            jarName        : "finaljar",
            serverWhitelist: [],
            serverBlacklist: [],
            spigotPlugin   : true,
            pssAddresses   : ["pss.mineheroes.net:9093", "testing.mineheroes.net:9093"],
            success        : true,
    ]
}

// The call(body) method in any file is exposed as a
// method with the same name as the file.
def call(body) {

    config = getDefaultConfig()

    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    try {
        run(config)
        currentBuild.result = currentBuild.currentResult
    } catch (ignored) {
        config.success = false
        currentBuild.result = "FAILURE"
        echo "Caught exception:\n${ignored}"
    }

    /*
     * notify slack/discord/...
     */
    notifyIntegration(config)
}

def run(config) {
    echo 'Starting Pipeline process'

    /*
     * Call process
     */
    process(config)

    /*
     * Post build actions
     */
    postBuild(config)
    echo 'Pipeline completed without errors'
}

def postBuild(config) {
    echo 'Starting post build actions'

    /*
     * Add config json to archive
     */
    if (config.success && currentBuild.currentResult == "SUCCESS") {
        publishDeployConfig(config)
    }

    /*
     * Notify PSS when success
     */
    if (config.spigotPlugin && config.success && currentBuild.currentResult == "SUCCESS") {
        notifyPSSDeployment(config)
    }
}