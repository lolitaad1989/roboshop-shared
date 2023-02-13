def call() {
    node {
        sh "rm -rf *"
        git branch: 'main', url: "git branch: 'main', url: 'https://github.com/lolitaad1989/${COMPONENT}.git'"
        env.APP = "angularjs"
        common.lintChecks()
        env.ARGS="-Dsonar.sources=."
        common.sonarChecks()
        common.testCases()   
        common.artifacts()
    }
}
