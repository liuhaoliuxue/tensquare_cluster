//git凭证ID
def git_auth = "b02667a5-ddcc-4826-b2e1-3c852f0529d2"

//git的url地址
def git_url = "git@192.168.1.133:itheima_group/tensquare.git"
//镜像版本号
def tag = "latest"
//定义harbor的地址
def harbor_url = "192.168.1.136:1080"
//定义私有库名称
def harbor_project = "tensquare"
//定义harbor的认证信息
def harbor_auth = "cb4c4d1f-f2f5-4ac0-a2ae-c08a1661835f"


node {

    //获取当前选择的项目名称
    def selectedProjectNames = "${project_name}".split(",")
    //获取当前选择的项目名称
    def selectedServers = "${publish_server}".split(",")

    stage('拉取代码') {
      checkout([$class: 'GitSCM', \
      branches: [[name: "*/${branch}"]], \
      doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [],\
      userRemoteConfigs: [[credentialsId: "${git_auth}", url: "${git_url}"]]])
   }
    stage('代码审查') {
        for(int i=0;i<selectedProjectNames.length;i++){
          //tensquare_eureka_server@10086
          def projectInfo = selectedProjectNames[i];
          //当前遍历项目的名称
          def currentProjectName = "${projectInfo}".split("@")[0]
          //当前遍历项目端口的名称
          def currentProjectPort = "${projectInfo}".split("@")[1]
        
        //引入一个名字叫sonar-scanner的全局工具
        def scannerHome = tool 'sonar-scanner'
        //引入一个名字叫sonarqube的系统配置（sonarqube服务器环境）
        withSonarQubeEnv('sonarqube') {
        //引入上面定义的全局变量
        sh """
          cd ${currentProjectName}
          ${scannerHome}/bin/sonar-scanner
           """
        }
      }

    }
    stage('编译，安装公共子工程') {
      sh "mvn -f tensquare_common clean install"
    }
    stage('编译，打包微服务工程，上传镜像') {
        for(int i=0;i<selectedProjectNames.length;i++){
          //tensquare_eureka_server@10086
          def projectInfo = selectedProjectNames[i];
          //当前遍历项目的名称
          def currentProjectName = "${projectInfo}".split("@")[0]
          //当前遍历项目端口的名称
          def currentProjectPort = "${projectInfo}".split("@")[1]
            // dockerfile:build会触发pom.xml中dockerfile-maven插件执行
            sh "mvn -f ${currentProjectName} clean package dockerfile:build"
            
            //定义镜像名字
            def imageName = "${currentProjectName}:${tag}"
            
            //对镜像打标签
            sh "docker tag ${imageName} ${harbor_url}/${harbor_project}/${imageName}"
            
            //把镜像推送到harbor仓库（使用凭证的方式登录）
            withCredentials([usernamePassword(credentialsId: "${harbor_auth}", passwordVariable: 'password', usernameVariable: 'username')]) {
              //使用凭证的方式登录harbor
              sh "docker login -u ${username} -p ${password} ${harbor_url}"
              //镜像的上传
              sh "docker push ${harbor_url}/${harbor_project}/${imageName}"
            
              sh "echo '镜像已经上传成功'"
              
              
              //遍历所有服务器分别部署
              for(int j=0;j<selectedServers.length;j++){
                //获取当前服务器名称
                def currentServerName = selectedServers[j]
                
                //添加微服务运行时的参数:spring.profiles.active 
                def activeProfile = "--spring.profiles.active="
                     if(currentServerName=="centos4"){
                          activeProfile = activeProfile+"centos4"
                     }else if(currentServerName=="centos5"){
                          activeProfile = activeProfile+"centos5"
                }
                
                //远程部署应用
                sshPublisher(publishers: [sshPublisherDesc(configName: "${currentServerName}", transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: "/ecapp/jenkins_shell/deploycluster.sh $harbor_url $harbor_project $currentProjectName $tag $currentProjectPort $activeProfile", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
                

              }
            }
      }
    }

}
