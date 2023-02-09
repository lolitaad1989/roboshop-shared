def lintchecks () {
    sh "echo Installing JSlint"
    sh "npm install jslint"
    sh "ls -ltr node_modules/jslint/bin"
    //sh "./node_modules/jslint/bin/jslint.js server.js"
    sh "echo lint checks"
}

pipeline {
    agent any
    stages {
        stage('Lint Checks') {
            steps {
                script {
                    lintchecks()
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