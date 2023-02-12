



def call (COMPONENT) 
{
    pipeline {
        agent any
        environment {
            SONAR = credentials('SONAR')
            NEXUS = credentials('NEXUS')
            SONAR_URL = "172.31.5.228"
            NEXUS_URL = "172.31.7.10"
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
            stage('test cases') {
                parallel {
                    stage('unit test') {
                        steps {
                            sh "echo unit testing..."
                        }
                    }
                    stage('Integration test') {
                        steps {
                            sh "echo integration testing..."
                        }   
                    }
                    stage('Functional test') {
                        steps {
                            sh "echo functional testing"
                        }
                    }
                }
            }
            stage ('Downloading the dependencies'){
                when { expression { env.TAG_NAME != null } } 
                steps {
                    sh "echo to install npm"
                    sh "npm install"
                    sh "zip ${COMPONENT}-${TAG_NAME}.zip node_modules server.js"
                    sh "ls -ltr"
                }
            }
            stage ('uploading the articrafts') {
                when { expression { env.TAG_NAME != null } } 
                steps {
                    sh "echo uploading the articrafts"
                    sh "curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://${NEXUS_URL}:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
                }
            }
        }
    }
}
