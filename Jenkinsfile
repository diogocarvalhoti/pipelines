public class BranchsUtil {
  public enum Types { FEATURE, HOTFIX, RELEASE }
  public enum Actions { START, FINISH }
  
  @NonCPS
  public static List<Enum> enumAsList(enumInstance){
      return Arrays.asList(enumInstance.values());
  }
  
}

node {
    
    def tipo
    def acao
    
    stage('Escolha tipo de Branch') {
        timeout(2) {
            tipo = input message: 'Escolha o tipo de branch:', 
            parameters: [choice(choices: BranchsUtil.enumAsList(BranchsUtil.Types), 
            description: '', name: 'tipo')]
        }
    }
   
    stage('Checkout') {
       checkout([: 'GitSCM', 
        branches: [[name: '*/development']], 
        doGenerateSubmoduleConfigurations: false, 
        extensions: [], submoduleCfg: [], userRemoteConfigs: 
            [[url: 'https://github.com/diogocarvalhoti/processjur.git']]])
    }
  
}
