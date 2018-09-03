def call(config) {

    stage("Publish deploy config") {
        String[] whitelistServers = config.serverWhitelist
        String[] blacklistServers = config.serverBlacklist

        def object = """{"deployable":${config.success && config.spigotPlugin},"serverWhitelist":${
            toStringArray(whitelistServers)
        },"serverBlacklist":${toStringArray(blacklistServers)}}"""

        echo 'Writing deploy options to target directory'

        node {
            writeFile file: 'target/deployOptions.json', text: object
            archiveArtifacts artifacts: 'target/deployOptions.json', fingerprint: true, onlyIfSuccessful: true
        }
    }

}

static def toStringArray(String[] array) {
    def result = "["

    for (int i = 0; i < array.length; i++) {
        result = result.concat("""\"${array[i]}\"""")
        if (i < array.length - 1) {
            result = result.concat(",")
        }
    }
    return result.concat("]")
}