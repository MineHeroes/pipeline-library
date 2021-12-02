def call(config) {
    stage("Archiving Artifacts") {
        if (config.archive) {
            if (config.archiveName != null) {
                echo "Renaming artifact ${config.targetDir}/${config.jarName}.jar to ${config.targetDir}/${config.archiveName}.jar"
                fileOperations([fileRenameOperation(destination: "${config.targetDir}/${config.archiveName}.jar", source: "${config.targetDir}/${config.jarName}.jar")])
                config.jarName = config.archiveName
            }

            echo "Archiving artifacts for ${env.JOB_NAME} #${env.BUILD_NUMBER}"
            archiveArtifacts artifacts: "${config.targetDir}/${config.jarName}.jar", fingerprint: true, onlyIfSuccessful: true
        }
    }
}