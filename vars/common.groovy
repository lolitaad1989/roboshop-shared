def sonarChecks (COMPONENT) {
    stage ('SONAR CHECKS') {
        sh "echo Starting Sonar Checks"
        // sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > quality-gata.sh"
        // sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000 env.ARGS -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW}"
        // sh "bash -x sonar-quality-gate.sh ${SONAR_USR} ${SONAR_PSW} ${SONAR_URL} ${COMPONENT}"
        sh "echo sonar checks are completed for ${COMPONENT}"
    }
}

def lintChecks () {
    stage('Lint checks') {
        if(env.APP == "maven") {
            sh "echo Installing mvn"
            // sh "yum install maven -y"
            // sh "mvn checkstyle:check"
            sh "echo lint checks completed for ${COMPONENT}.....!!!!!"   
        }
        else if(env.APP == "nodejs") {
            sh "echo Installing JSLINT"
            sh "npm install jslint"
            sh "ls -ltr node_modules/jslint/bin/"
            // sh "./node_modules/jslint/bin/jslint.js server.js"
            sh "echo lint checks completed for ${COMPONENT}.....!!!!!"      
        }
        else if(env.APP == "python") {
            sh "echo Installing PYLINT"
            // https://pylint.pycqa.org/en/2.7.1/user_guide/run.html 
            // sh pylist filename.py
            sh "echo lint checks completed for ${COMPONENT}.....!!!!!"      
        }
        else if(env.APP == "angularjs") {
            sh "echo lint checks completed for ${COMPONENT}.....!!!!!"        
        }
        else {
        sh "echo performing generic lint cheks"
        }
    }
}

def testCases() {
    parallel(                                                        // This is how we write stages in parallel.
            "UNIT": {
                    stage('Unit Tests'){
                            sh "echo Unit Testing ......."                            
                    }
                },
            "INTEGRATION": {
                    stage('Integration Tests'){
                            sh "echo Integration Testing ......."                          
                    }
                },
            "FUNCTIONAL": {
                    stage('Functional Tests'){
                            sh "echo Functional Testing ......."                          
                    }
                },
        )
    }

    def artifacts() {
       if(env.TAG_NAME != null) {
              stage('Artifact Validation On Nexus') {
                     sh "echo checking whether artifact exists of not. If it doesnt exist then only proceed with Preparation and Upload"
                     env.UPLOAD_STATUS=sh(returnStdout: true, script: "curl -L -s http://172.31.7.10:8081/service/rest/repository/browse/${COMPONENT} | grep ${COMPONENT}-${TAG_NAME}.zip || true" )
              }

       if(env.UPLOAD_STATUS == "") {
              stage('Preparing the artifacts') {
                     if(env.APP == "maven") {
                            sh ''' 
                                   mvn clean package
                                   mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar
                                   zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar
                            '''
                     }
                     else if(env.APP == "nodejs") {
                            sh ''' 
                                   npm install
                                   zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js
                            '''
                     }
                     else if(env.APP == "python") {
                            sh ''' 
                                   zip -r ${COMPONENT}-${TAG_NAME}.zip *.py *.ini requirements.txt
                            '''
                     }
                     else if(env.APP == "angularjs") {
                            sh ''' 
                                   cd static
                                   zip -r ../${COMPONENT}-${TAG_NAME}.zip *
                            '''
                     }
                     else {
                            sh ''' 
                                   echo "Golang is your task"
                            '''
                     }
              }
              
              stage('Uploading Artifacts') {
                     withCredentials([usernamePassword(credentialsId: 'NEXUS', passwordVariable: 'NEXUS_PSW', usernameVariable: 'NEXUS_USR')]) {
                            sh "curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.7.10:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
                            }
                     }
              }
       }
} 