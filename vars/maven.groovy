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
            stage('Artifact validation on Nexus') {
                when { 
                    expression { env.TAG_NAME != null } 
                } 
                steps {
                    sh "echo Checking whether artifact exists or not. If it doesnt exist then only proceed with preparation and upload"
                    script {
                        env.UPLOAD_STATUS=sh(returnStdout: true, script: "curl -L -s http://${NEXUS_URL}:8081/service/rest/repository/browse/${COMPONENT} | grep ${COMPONENT}-${TAG_NAME}.zip || true ")
                        print UPLOAD_STATUS
                    }
                }
            }
            stage ('Downloading the dependencies'){
                when { 
                    expression { env.TAG_NAME != null } 
                    expression { env.UPLOAD_STATUS == "" }
                } 
                steps {
                    sh "mvn clean package"
                    sh "mv /shipping/target/${COMPONENT}-1.0.jar ${COMPONENT}.jar"
                    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar"
                    sh "ls -ltr"
                }
            }
            stage ('uploading the articrafts') {
                when { 
                    expression { env.TAG_NAME != null } 
                    expression { env.UPLOAD_STATUS == "" }
                } 
                steps {
                    sh "echo uploading the articrafts"
                    sh "curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://${NEXUS_URL}:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"

                }
            }
        }
    }
}
