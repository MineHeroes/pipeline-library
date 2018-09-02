def call(config) {
    String result = currentBuild.result
    String duration = currentBuild.durationString.replace(' and counting', '')

    if (result == "SUCCESS" && config.success) {
        notifySuccessful(env.JOB_NAME, env.BUILD_NUMBER, result.toLowerCase().capitalize(), duration, env.BUILD_URL)
    } else {
        notifyFailed(env.JOB_NAME, env.BUILD_NUMBER, result.toLowerCase().capitalize(), duration, env.BUILD_URL)
    }
}

def notifySuccessful(String jobName, String buildNumber, String result, String duration, String url) {
    slackSend(color: 'good', message: "${jobName} - #${buildNumber} ${result} after ${duration} (<${url}|Open>)")

    withCredentials([string(credentialsId: 'discord_hook', variable: 'discWebhook')]) {
        discordSend description: "${jobName} - #${buildNumber} ${result} after ${duration} ([Open](${url}))", successful: true, webhookURL: discWebhook
    }
}

def notifyFailed(String jobName, String buildNumber, String result, String duration, String url) {
    slackSend(color: 'danger', message: "${jobName} - #${buildNumber} ${result} after ${duration} (<${url}|Open>)")

    withCredentials([string(credentialsId: 'discord_hook', variable: 'discWebhook')]) {
        discordSend description: "${jobName} - #${buildNumber} ${result} after ${duration} ([Open](${url}))", successful: false, webhookURL: discWebhook
    }
}
