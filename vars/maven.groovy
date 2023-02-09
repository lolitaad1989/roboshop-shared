def lintchecks (COMPONENT) {
    // sh "echo Installing mvn"
    // sh "yum install maven -y"
    // sh "mvn checkstyle:check"
    //sh "./node_modules/jslint/bin/jslint.js server.js"
    sh "echo lint checks are completed for ${COMPONENT}"
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
            stage ('Downloading the dependencies'){
                steps {
                    sh "npm install"
                }
            }
        }
    }
}
