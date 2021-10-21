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
                String content = readFile("gradle.properties")
                Properties properties = new Properties()
                properties.load(new StringReader(content))
                String version = properties.version

                stage('Version Check'){
                    echo "Checking version '${version}' from gradle.properties"

                    if (BRANCH_NAME != config.mainBranch) {
                        version.replace('-SNAPSHOT', '')
                        version = "${version}_${BRANCH_NAME}-SNAPSHOT"
                        echo "Set version to ${version}"
                    } else {
                        echo "Branch is ${config.mainBranch}. No version change required."
                    }
                }

                stage('Clean') {
                    echo 'Cleaning build directories'
                    sh "./gradlew clean"
                }

                stage('Gradle Tasks') {
                    echo 'Executing gradle tasks'
                    String[] tasks = config.gradleTasks
                    for (String task : tasks) {
                        timeout(time: 15, unit: 'MINUTES') {
                            sh "./gradlew -Pversion=\"${version}\" ${task}"
                        }
                    }
                }
                stepArchiveArtifacts(config)
            }
        }
    }

}
