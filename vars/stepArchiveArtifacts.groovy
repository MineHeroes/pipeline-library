def call(config) {
    if (config.archive) {
        echo "Archiving artifacts for ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        archiveArtifacts artifacts: "target/${config.jarName}.jar", fingerprint: true, onlyIfSuccessful: true
    }
}