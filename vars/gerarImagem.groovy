#!groovy
import br.gov.mds.pipeline.GitFlow

def call(args) {

    GITLAB_LOGIN_SSH = 'gitlab-login-ssh'
    BUILD_NUMBER = "${BUILD_NUMBER}"


    def label = "teste-gerar-imagem-${UUID.randomUUID().toString()}"

    podTemplate(label: label, serviceAccount: 'jenkins') {


        node(label) {
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

            stage('Listar Tags') {
                def gitflow = new GitFlow()
                Integer idProject = gitflow.getIdProject(namespace)

                List tags = gitflow.getTags(idProject)

                def TAG = input message: 'Escolha a tag', parameters: [choice(choices: tags, description: '', name: '')]

            }

        }
    }
}


call(
        gitRepositorySSH: 'git@sugitpd02.mds.net:sistemas/cgsi/senarc/sisbapi2.git'
)
