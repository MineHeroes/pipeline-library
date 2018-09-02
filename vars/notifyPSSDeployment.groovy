def call(config) {
    echo 'Triggering Jenkins refresh'
    String[] addresses = config.pssAddresses
    for (int i = 0; i < addresses.length; i++) {
        sh 'curl --data "jobtype=jenkins.branch.OrganizationFolder&jobname=Mineheroes" ${addresses[i]} &'
    }
}