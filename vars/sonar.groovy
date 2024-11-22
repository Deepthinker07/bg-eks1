def var() {
    withSonarQubeEnv ('sonarserver') {
                    sh '''
                    $scanner/bin/sonar-scanner \
                    -Dsonar.projectName=boardgame \
                    -Dsonar.projectKey=boardgamekey \
                    -Dsonar.java.binaries=target/classes 
                    '''
                }
}