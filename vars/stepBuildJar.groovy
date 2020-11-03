def call(config) {

    stage("Build and Deploy") {
        echo 'Starting build and deploy process.'
        timeout(time: 20, unit: 'MINUTES') {
            def pom = readMavenPom file: 'pom.xml'
            def command = 'MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1" mvn -T 2C'

            if (config.useMavenProfile == null) {
                if (config.javadoc == true) {
                    echo 'Including JavaDoc'
                    command = command.concat(' javadoc:jar')
                }
                if (config.source == true) {
                    echo 'Including Source'
                    command = command.concat(' source:jar')
                }
            } else {
                echo 'Using profile '.concat(config.useMavenProfile)
                command = command.concat(' --activate-profiles ').concat(config.useMavenProfile)
            }

            command = command.concat(" --no-transfer-progress -Dbuild.number=-${BRANCH_NAME}_B${env.BUILD_NUMBER}")
            if (pom.getDistributionManagement() != null && config.deploy == true) {
                echo 'Deploying results to Nexus'
                command = command.concat(' deploy')
            } else {
                echo 'Creating jar package'
                command = command.concat(' package')
            }

            sh "${command}"
        }
    }

}