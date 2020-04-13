def call(config) {
    stage("Build Trigger") {
        String[] projects = config.triggerSucceedingBuilds
        if (projects.length == 0) {
            echo 'No succeeding builds to trigger'
            return
        }

        node {
            for (int i = 0; i < projects.length; i++) {
                echo """Triggering build ' ${projects[i]} '"""
                build job: projects[i], propagate: false, quietPeriod: 10, wait: false
            }
        }
    }
}