/**
 * https://github.com/alvin-huang-jenkinsworld-org/standard-library
 */
class standardBuild extends baseBuild {

    def process(config) {
        node {
            properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: config.keepNumberOfBuilds, daysToKeepStr: '', numToKeepStr: ''))])

            //add maven from /opt/maven/bin from the tool config to path
            env.MVN_HOME = "${tool name: 'Opt Maven', type: 'maven'}"
            env.PATH = "${env.MVN_HOME}bin:${env.PATH}"

            echo 'Using Maven Version:'
            sh 'mvn --version'

            stage('Checkout') {
                echo 'Checking out SCM'
                checkout scm
            }
            dir(path: config.projectDir) {
                stepEffectivePom()
                setPOMVersionStep(config)
                stepCleanWorkspace()
                stepBuildJar(config)
                stepArchiveArtifacts(config)
            }
        }
    }

}
