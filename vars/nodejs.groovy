def lintchecks (COMPONENT) {
    sh "echo Installing JSlint"
    sh "npm install jslint"
    sh "ls -ltr node_modules/jslint/bin"
    //sh "./node_modules/jslint/bin/jslint.js server.js"
    sh "echo lint checks are completed for ${COMPONENT}"
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
                        env.ARGS="-Dsonar.sources=."
                        common.sonarChecks(COMPONENT)
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
