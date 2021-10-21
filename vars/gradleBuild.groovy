class gradleBuild extends baseBuild {

    def process(config) {
        node {
            properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: config.keepNumberOfBuilds, daysToKeepStr: '', numToKeepStr: ''))])

            //change JAVA_HOME according to set jdk
            env.JAVA_HOME = tool name: "${config.jdk}", type: 'jdk'
            echo "Using Java Version in ${env.JAVA_HOME}"

            stage('Checkout') {
                echo 'Checking out SCM'
                checkout scm
            }
            dir(path: config.projectDir) {

                stage('Version Check'){
                    echo 'Checking version in gradle.properties'
                    String content = readFile("gradle.properties")
                    Properties properties = new Properties()
                    properties.load(new StringReader(content))
                    String version = properties.version

                    if (BRANCH_NAME != config.mainBranch) {
                        version.replace('-SNAPSHOT', '')
                        version = "${version}_${BRANCH_NAME}-SNAPSHOT"
                        echo "Set version to ${version}"
                    } else {
                        echo "Branch is ${config.mainBranch}. No version change required."
                    }
                }

                stage('Clean') {
                    echo 'Cleaning caches'
                    sh "./gradlew clean cleanCache"
                }

                stage('Gradle Task') {
                    echo 'Executing gradle task'
                    sh "./gradlew -Pversion=\"${version}\" ${config.gradleTask}"
                }
                stepArchiveArtifacts(config)
            }
        }
    }

}
