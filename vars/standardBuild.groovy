/**
 * https://github.com/alvin-huang-jenkinsworld-org/standard-library
 */
class standardBuild extends baseBuild {

    def process(config) {
        node {
            try {
                stage('Checkout') {
                    echo 'Checking out SCM'
                    checkout scm
                }
                stage('SetVersion') {
                    setPOMVersionStep(config)
                }
                stage('Clean') {
                    stepCleanWorkspace()
                }
                stage('Build/Deploy') {
                    stepBuildJar(config)
                }
                stage('Archive Jars') {
                    stepArchiveArtifacts(config)
                }
            } catch (err) {
                echo 'Pipeline did not complete all tasks successfully!'
                config.success = false
                err.printStackTrace()
            }
        }
    }

}