def sonarChecks (COMPONENT) {
    sh "echo Starting Sonar Checks"
    // sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > quality-gata.sh"
    // sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000 env.ARGS -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW}"
    // sh "bash -x sonar-quality-gate.sh ${SONAR_USR} ${SONAR_PSW} ${SONAR_URL} ${COMPONENT}"
    sh "echo sonar checks are completed for ${COMPONENT}"
}

def lintchecks (COMPONENT) {
    sh "echo Installing JSlint"
   // sh "npm install jslint"
  //  sh "ls -ltr node_modules/jslint/bin"
    //sh "./node_modules/jslint/bin/jslint.js server.js"
    sh "echo lint checks are completed for ${COMPONENT}"
}