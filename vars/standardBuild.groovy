/**
 * https://github.com/alvin-huang-jenkinsworld-org/standard-library
 */
class standardBuild extends baseBuild {

    def process(config) {
        node {
            properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: config.keepNumberOfBuilds, daysToKeepStr: '', numToKeepStr: ''))])
            stage('Checkout') {
                echo 'Checking out SCM'
                checkout scm
            }
            dir(path: config.projectDir) {
                setPOMVersionStep(config)
                stepCleanWorkspace()
                stepBuildJar(config)
                stepArchiveArtifacts(config)
            }
        }
    }

}
