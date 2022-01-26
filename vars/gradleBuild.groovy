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
                def properties = readProperties file: 'gradle.properties'
                String version = properties.version

                stage('Version Check') {
                    echo "Checking version '${version}' from gradle.properties"
                    if (config.fixPOMVersion) {
                        if (BRANCH_NAME != config.mainBranch) {
                            version = version.replace('-SNAPSHOT', '')
                            version = "${version}_${BRANCH_NAME}-SNAPSHOT"
                            properties.setProperty("version", version)
                            properties.store(new FileWriter("${WORKSPACE}/gradle.properties", false), null)
                            echo "Set version to ${version}"
                        } else {
                            echo "Branch is ${config.mainBranch}. No version change required."
                        }
                    } else {
                        echo "Not attempting version change by config setting!"
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
                            sh "./gradlew -Pbuild.number=-${BRANCH_NAME}_B${env.BUILD_NUMBER} ${task}"
                        }
                    }
                }
                stepArchiveArtifacts(config)
            }
        }
    }

}
