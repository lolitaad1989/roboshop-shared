def lintchecks (COMPONENT) {
    sh "echo Installing JSlint"
    //sh "npm install jslint"
   // sh "ls -ltr node_modules/jslint/bin"
    //sh "./node_modules/jslint/bin/jslint.js server.js"
    sh "echo lint checks are completed for ${COMPONENT}"
}

def sonarChecks (COMPONENT) {
    sh "echo Starting Sonar Checks"
    sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000 -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW} "
    sh "echo sonar checks are completed for ${COMPONENT}"
}
def call (COMPONENT) 
{
    pipeline {
        agent any
        environment {
            SONAR = credentials('SONAR')
            SONAR_URL = "172.31.5.228"
        }
        stages {
            stage('Lint Checks') {
                steps {
                    script {
                        lintchecks(COMPONENT)
                    }
                }
            }
            stage('Sonar Checks') {
                steps {
                    script {
                        sonarChecks(COMPONENT)
                    }
                }
            }
            stage ('Downloading the dependencies'){
                steps {
                    sh "echo to install npm"
                   // sh "npm install"
                }
            }
        }
    }
}
