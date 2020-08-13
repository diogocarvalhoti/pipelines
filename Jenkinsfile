#!groovy

import com.cloudbees.groovy.cps.NonCPS

import java.util.regex.Matcher
import java.util.regex.Pattern

def call(args) {

    GITLAB_LOGIN_SSH = '3eaff500-4fdb-46ac-9abb-7a1fbbd88f5f'
    // GITLAB_LOGIN_SSH = 'gitlab-login-ssh'
    BUILD_NUMBER = "${BUILD_NUMBER}"


    // def label = "teste-gerar-imagem-${UUID.randomUUID().toString()}"

    // podTemplate(label: label, serviceAccount: 'jenkins') {

    node() {
        stage('Checkout código fonte') {
            cleanWs()
            checkout([$class                           : 'GitSCM', branches: [[name: '*/develop']],
                      doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [
                    [
                            credentialsId: GITLAB_LOGIN_SSH,
                            refspec      : '+refs/heads/*:refs/remotes/origin/* +refs/merge-requests/*/head:refs/remotes/origin/merge-requests/*',
                            url          : args.gitRepositorySSH]
            ]]
            )

            sh 'git config --global user.email \"gcm_cgsi@cidadania.gov.br\"'
            sh 'git config --global user.name \"Gerência de Configuração e Mudança\"'
        }

        stage('Selecione a TAG') {

            if (args.gitTag == null) {
                tags = sh(script: "git tag --sort=-v:refname", returnStdout: true).trim()
                try {
                    timeout(time: 30, unit: 'SECONDS') {
                        args.gitTag = input(
                                id: 'gitBranch', message: 'Favor selecionar a tag', parameters: [
                                [$class: 'hudson.model.ChoiceParameterDefinition', choices: tags, description: '', name: 'GIT_TAG']
                        ])
                    }
                } catch (err) {
                    def user = err.getCauses()[0].getUser()
                    currentBuild.result = 'FAILURE'
                    if ('SYSTEM' == user.toString()) { // SYSTEM = timeout.
                        error("""===========\nBuild abortada por timeout aguardando entrada. Favor selecionar a branch no tempo concedido!\n===========""")
                    } else {
                        error("Abortada por: [${user}]")
                    }
                }
                sh(script: "git checkout ${args.gitTag}")
            }
        }
    }
    // }
}


@NonCPS
def recuperarNamespace(repository) {
    final String regex = "\\:(.*?).git";

    final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
    final Matcher matcher = pattern.matcher(repository);

    while (matcher.find()) {
        return matcher.group(1).replace("/", "%2F");
    }
    return ""
}

call(
        gitRepositorySSH: 'git@sugitpd02.mds.net:gcm_cgsi/municipio-mais-cidadao.git'
)
