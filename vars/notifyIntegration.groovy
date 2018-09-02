def call(String jobName, String buildNumber, String result, String duration, String url) {
    duration = duration.replace(' and counting', '')

    if (result == "SUCCESS") {
        notifySuccessful(jobName, buildNumber, result.toLowerCase().capitalize(), duration, url)
    } else {
        notifyFailed(jobName, buildNumber, result.toLowerCase().capitalize(), duration, url)
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
