import groovy.json.JsonBuilder

def call(config) {

    stage("Publish deploy config") {
        String[] whitelistServers = config.serverWhitelist
        String[] blacklistServers = config.serverBlacklist

        def builder = new JsonBuilder()
        def root = builder deployable: config.success, serverWhitelist: whitelistServers, serverBlacklist: blacklistServers

        echo 'Writing deploy options to target directory'

        node {
            writeJSON file: 'target/deployOptions.json', json: root.toString()
            archiveArtifacts artifacts: 'target/deployOptions.json', fingerprint: true, onlyIfSuccessful: true
        }
    }

}