def call(config) {
    String[] addresses = config.pssAddresses
    if (addresses.length == 0) {
        echo 'Skipping Jenkins refresh'
        return
    }

    echo 'Triggering Jenkins refresh'
    for (int i = 0; i < addresses.length; i++) {
        sh 'curl --data "jobtype=jenkins.branch.OrganizationFolder&jobname=Mineheroes" ${addresses[i]} &'
    }
}