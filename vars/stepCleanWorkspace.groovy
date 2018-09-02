def call() {
    stage("Clean") {
        echo 'Cleaning target/ directory'
        sh 'mvn clean'
    }
}