def call(config) {
    if (config.success && config.archive && currentBuild.currentResult == "SUCCESS") {
        echo "Archiving artifacts for ${env.JOB_NAME} #${env.BUILD_NUMBER}"
        archiveArtifacts artifacts: "target/${config.jarName}.jar", fingerprint: true, onlyIfSuccessful: true
    }
}