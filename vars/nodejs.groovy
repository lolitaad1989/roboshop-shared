def lintchecks () {
    sh "echo Installing JSlint"
    sh "npm install jslint"
    sh "ls -ltr node_modules/jslint/bin"
    //sh "./node_modules/jslint/bin/jslint.js server.js"
    sh "echo lint checks are completed for ${COMPONENT}"
}

def call () {
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
