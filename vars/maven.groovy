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
                       // env.ARGS="-Dsonar.java.binaries=target/"
                        //sh mvn clean complile
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
                    sh "mvn clean package"
                }
            }
            stage ('uploading the articrafts') {
                when { expression { env.TAG_NAME != null } } 
                steps {
                    sh "echo uploading the articrafts"
                }
            }
        }
    }
}
