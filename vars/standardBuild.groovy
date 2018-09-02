/**
 * https://github.com/alvin-huang-jenkinsworld-org/standard-library
 */
class standardBuild extends baseBuild {

    def process(config) {
        node {
            stage('Checkout') {
                echo 'Checking out SCM'
                checkout scm
            }
            setPOMVersionStep(config)
            stepCleanWorkspace()
            stepBuildJar(config)
            stepArchiveArtifacts(config)
        }
    }

}