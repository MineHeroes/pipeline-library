static def getDefaultConfig() {
    return [
            javadoc        : true,
            source         : true,
            archive        : true,
            fixPOMVersion  : true,
            jarName        : "finaljar",
            serverWhitelist: [],
            serverBlacklist: [],
            pssAddresses   : ["pss.mineheroes.net:9093", "testing.mineheroes.net:9093"],
    ]
}

// The call(body) method in any file is exposed as a
// method with the same name as the file.
def call(body) {

    config = getDefaultConfig()

    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()

    run(config)
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
}

def postBuild(config) {
    echo 'Starting post build actions'

    /*
     * archive jars when success
     */
    if (config.archive && env.currentResult == "SUCCESS") {
        archiveArtifacts(config)
    }

    /*
     * Add config json to archive
     */
    if (env.currentResult == "SUCCESS") {
        publishDeployConfig(config)
    }

    /*
     * Notify PSS when success
     */
    if (env.currentResult == "SUCCESS") {
        notifyPSSDeployment()
    }

    /*
     * notify slack/discord/...
     */
    notifyIntegration()
}