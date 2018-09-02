/**
 * https://github.com/alvin-huang-jenkinsworld-org/standard-library
 */
class standardBuild extends baseBuild {

    def process(config) {
        pipeline {
            agent any

            tools {
                maven 'maven'
                jdk 'jdk8'
            }

            stage('Checkout') {
                steps {
                    echo 'Checking out SCM'
                    checkout scm
                }
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
        }
    }

}