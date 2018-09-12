def call(config) {
    if (config.fixPOMVersion) {
        stage("Set POM version") {
            echo 'Checking version of project'
            def pom = readMavenPom file: 'pom.xml'
            def pomVersion = pom.getVersion().replace("-SNAPSHOT", "")

            // If name of jar file was not set, overwrite it with artifact id
            if (config.jarName == null) {
                config.jarName = pom.getArtifactId()
            }

            if (BRANCH_NAME != 'master') {
                pomVersion = "${pomVersion}_${BRANCH_NAME}-SNAPSHOT"
                echo "Set version to ${pomVersion}"
                sh "mvn versions:set -DnewVersion=${pomVersion}"
            } else {
                echo 'Branch is master. No version change required.'
            }
        }
    }
}