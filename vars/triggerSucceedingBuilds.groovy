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
                echo """Triggering build 'MineHeroes/${projects[i]}/master'"""
                build job: "MineHeroes/${projects[i]}/master", propagate: false, quietPeriod: 10, wait: false
            }
        }
    }
}