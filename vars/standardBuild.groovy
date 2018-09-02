/**
 * https://github.com/alvin-huang-jenkinsworld-org/standard-library
 */
class standardBuild extends baseBuild {

    def process(config) {
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
        } catch (err) {
            throw err
        }
    }

}