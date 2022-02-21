/**
 * https://github.com/alvin-huang-jenkinsworld-org/standard-library
 */
class standardBuild extends baseBuild {

    def process(config) {
        properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: config.keepNumberOfBuilds, daysToKeepStr: '', numToKeepStr: ''))])

        //add maven from /opt/maven/bin from the tool config to path
        env.MVN_HOME = "${tool name: 'Opt Maven', type: 'maven'}"
        env.PATH = "${env.MVN_HOME}bin:${env.PATH}"

        //change JAVA_HOME according to set jdk
        env.JAVA_HOME = tool name: "${config.jdk}", type: 'jdk'
        echo "Using Java Version in ${env.JAVA_HOME}"

        echo 'Using Maven Version:'
        sh 'mvn --version'

        dir(path: config.projectDir) {
            stepEffectivePom()
            setPOMVersionStep(config)
            stepCleanWorkspace()
            stepBuildJar(config)
            stepArchiveArtifacts(config)
        }
    }

}
