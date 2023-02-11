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
                        lintchecks(COMPONENT)
                    }
                }
            }
            stage('Sonar Checks') {
                steps {
                    script {
                        env.ARGS="-Dsonar.java.binaries=target/"
                        sh mvn clean complile
                        common.sonarChecks(COMPONENT) 
                    }
                }
            }
            stage ('Downloading the dependencies'){
                steps {
                    sh "mvn clean package"
                }
            }
        }
    }
}
