def call(config) {

    stage("Build and Deploy") {
        echo 'Starting build and deploy process.'
        timeout(time: 20, unit: 'MINUTES') {
            def pom = readMavenPom file: 'pom.xml'
            def command = "mvn"

            if (config.javadoc == true) {
                echo 'Including JavaDoc'
                command = "${command} javadoc:jar"
            }
            if (config.source == true) {
                echo 'Including Source'
                command = "${command} source:jar"
            }
            command = "${command} -Dbuild.number=-${BRANCH_NAME}_B${env.BUILD_NUMBER}"
            if (pom.getDistributionManagement() != null) {
                echo 'Deploying results to Nexus'
                command = "${command} deploy"
            } else {
                command = "${command} install"
            }

            sh "${command}"
        }
    }

}