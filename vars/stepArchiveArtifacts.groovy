def call(config) {
    stage("Archiving Artifacts") {
        if (config.archive) {
            echo "Archiving artifacts for ${env.JOB_NAME} #${env.BUILD_NUMBER}"
            archiveArtifacts artifacts: "target/${config.jarName}.jar", fingerprint: true, onlyIfSuccessful: true
        }
    }
}