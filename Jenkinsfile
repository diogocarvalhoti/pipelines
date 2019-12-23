public class BranchsUtil {
  public enum Types { FEATURE, HOTFIX, RELEASE }
  public enum Actions { START, FINISH }
  
  @NonCPS
  public static List<Enum> enumAsList(enumInstance){
      return Arrays.asList(enumInstance.values());
  }
  
}

node {
    
    stage('Checkout') {
       cleanWs()
       checkout([$class: 'GitSCM', branches: [[name: '*/develop']], 
        doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[
           credentialsId: '3eaff500-4fdb-46ac-9abb-7a1fbbd88f5f', 
           refspec: '+refs/heads/*:refs/remotes/origin/* +refs/merge-requests/*/head:refs/remotes/origin/merge-requests/*', 
           url: 'git@sugitpd02.mds.net:gcm_cgsi/sispaa.git']]]
        )
    }
    
    stage('Escolha tipo de Branch') {
        timeout(2) {
            TIPO = input message: 'Escolha o tipo de branch:', 
            parameters: [choice(choices: BranchsUtil.enumAsList(BranchsUtil.Types), 
            description: '', name: 'tipo')]
        }
    }
    
    stage('Escolha a ação') {
        timeout(2) {
            ACAO = input message: 'Escolha a ação:', 
            parameters: [choice(choices: BranchsUtil.enumAsList(BranchsUtil.Actions), 
            description: '', name: 'acao')]
        }
    }
    
    if(TIPO == "FEATURE"){
        FEATURE_NAME = input(
          id: 'userInput', message: 'Nome da feature',
          parameters: [
            string(
              description: 'Nome da feature',
              name: 'Nome da feature'
            )
        ])
        
        if(ACAO == "START") {
            withCredentials([sshUserPrivateKey(credentialsId: '3eaff500-4fdb-46ac-9abb-7a1fbbd88f5f', keyFileVariable: '', passphraseVariable: '', usernameVariable: '')]) {
                sh "git config --global http.sslVerify false"
                // sh "git config --global credential.username $GITLAB_USER"
                // sh "git config --global credential.helper \'!f() { echo \"password=\'$PASS\'\"; }; f\'"
                // sh "git config gitflow.branch.develop develop"
                sh "git flow init -d &> /dev/null"
                // sh "git checkout develop"
                sh "git flow feature start "+FEATURE_NAME
                sh "git flow feature publish "+FEATURE_NAME
            }
        } else {
            println "O fechamento da feature deverá ser feito manualmente através do Merge Request."
            
        }
    }
}