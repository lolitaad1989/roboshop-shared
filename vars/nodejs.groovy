def lintchecks (COMPONENT) {
    sh "echo Installing JSlint"
    sh "npm install jslint"
    sh "ls -ltr node_modules/jslint/bin"
    //sh "./node_modules/jslint/bin/jslint.js server.js"
    sh "echo lint checks are completed for ${COMPONENT}"
}

def sonarChecks (COMPONENT) {
    sh "Starting Sonar Checks"
    sonar-scanner -Dsonar.host.url=http://172.31.5.228:9000 -Dsonar.sources=. -Dsonar.projectKey=catalogue -Dsonar.login=admin -Dsonar.password=password
    sh "echo sonar checks are completed for ${COMPONENT}"
}
def call (COMPONENT) 
{
    pipeline {
        agent any
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
                    sh "npm install"
                }
            }
        }
    }
}
