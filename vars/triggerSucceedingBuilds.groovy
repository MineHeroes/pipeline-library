def call(config) {
    stage("Build Trigger") {
        String[] projects = config.triggerSucceedingBuilds
        if (projects.length == 0) {
            echo 'No succeeding builds to trigger'
            return
        }
        //Only trigger when master branch
        if (BRANCH_NAME != 'master') {
            echo 'Not on master branch. Skipping build trigger.'
            return
        }

        node {
            for (int i = 0; i < projects.length; i++) {
                String projectName = projects[i]
                if (!projectName.contains("/")) {
                    projectName = projectName + "/master"
                }

                echo """Triggering build 'MineHeroes/${projectName}'"""
                build job: "MineHeroes/${projectName}", propagate: false, quietPeriod: 10, wait: false
            }
        }
    }
}