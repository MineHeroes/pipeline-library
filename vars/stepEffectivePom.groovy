def call() {
    stage("Generate Effective POM") {
        echo 'Checking parent POM'
        def pom = readMavenPom file: 'pom.xml'

        def parent = pom.getParent()
        if (parent == null) {
            echo 'No parent project... Skipping'
            return
        }

        //Generate the effective pom and replace the pom.xml file
        echo "Generating effective pom"
        sh "mvn -N help:effective-pom -Doutput=pom.xml"
    }

}