pipeline {
    agent any 
    tools {
        maven 'maven3'
    }
    environment {
        scanner = tool 'sonarscanner'
        userName = 'deepthinker07'
        imgName = 'bgimg'
    }
    stages {
        stage ('cleanWs') {
            steps {
                cleanWs()
            }
        }
        stage ('git checkout') {
            steps {
                git credentialsId: 'sa', url: 'https://github.com/Deepthinker07/bg-eks1.git'
            }
        }
        stage ('code compile') {
            steps {
                sh 'mvn compile'
            }
        }
        stage ('unit test') {
            steps {
                sh 'mvn test'
            }
        }
        stage ('static code analysis') {
            steps {
                withSonarQubeEnv ('sonarserver') {
                    sh '''
                    $scanner/bin/sonar-scanner \
                    -Dsonar.projectName=boardgame \
                    -Dsonar.projectKey=boardgamekey \
                    -Dsonar.java.binaries=target/classes 
                    '''
                }
            }
        }
        stage ('code package') {
            steps {
                sh 'mvn package'
            }
        }
        stage ('docker image build') {
            steps {
                sh 'docker build -t $userName/$imgName .'
            }
        }
        stage ('push image to dockerhub') {
            steps {
                withDockerRegistry(credentialsId: 'sa2', url: '') {
                    sh 'docker push $userName/$imgName'
                }    
            }
        }
        stage ('deploy to eks') {
            steps {
                withKubeConfig(caCertificate: '', clusterName: 'sathish-eks', contextName: '', credentialsId: 'kub', namespace: 'webapps', restrictKubeConfigAccess: false, serverUrl: 'https://DBC183504E8FD5E8DE75305339976D8C.gr7.ap-southeast-1.eks.amazonaws.com') {
                    sh 'kubectl apply -f deploy-svc.yml'
                    sleep 60
                }
            }
        }
        stage ('verify to eks') {
            steps {
                withKubeConfig(caCertificate: '', clusterName: 'sathish-eks', contextName: '', credentialsId: 'kub', namespace: 'webapps', restrictKubeConfigAccess: false, serverUrl: 'https://DBC183504E8FD5E8DE75305339976D8C.gr7.ap-southeast-1.eks.amazonaws.com') {
                    sh 'kubectl get svc'
                }
            }
        }
            
    }  
}
// skip, parallel, slack & post 