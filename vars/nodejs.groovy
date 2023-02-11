



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
                        common.lintchecks(COMPONENT)
                    }
                }
            }
            stage('Sonar Checks') {
                steps {
                    script {
                        //env.ARGS="-Dsonar.sources=."
                        common.sonarChecks(COMPONENT)
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
