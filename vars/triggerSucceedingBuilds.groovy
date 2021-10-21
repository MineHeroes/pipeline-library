def call(config) {
    stage("Build Trigger") {
        String[] projects = config.triggerSucceedingBuilds
        if (projects.length == 0) {
            echo 'No succeeding builds to trigger'
            return
        }
        //Only trigger when main branch
        if (BRANCH_NAME != config.mainBranch) {
            echo "Not on ${config.mainBranch} branch. Skipping build trigger."
            return
        }

        node {
            for (int i = 0; i < projects.length; i++) {
                String projectName = projects[i]
                if (!projectName.contains("/")) {
                    projectName = projectName + "/${config.mainBranch}"
                }

                echo """Triggering build 'MineHeroes/${projectName}'"""
                build job: "MineHeroes/${projectName}", propagate: false, quietPeriod: 10, wait: false
            }
        }
    }
}