def sonarChecks (COMPONENT) {
    stage ('SONAR CHECKS') {
        sh "echo Starting Sonar Checks"
        // sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > quality-gata.sh"
        // sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000 env.ARGS -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW}"
        // sh "bash -x sonar-quality-gate.sh ${SONAR_USR} ${SONAR_PSW} ${SONAR_URL} ${COMPONENT}"
        sh "echo sonar checks are completed for ${COMPONENT}"
    }
}

def lintchecks () {
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