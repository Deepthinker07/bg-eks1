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
                withKubeConfig(caCertificate: '', clusterName: 'my-eks', contextName: '', credentialsId: 'k8s-secret', namespace: 'webapps', restrictKubeConfigAccess: false, serverUrl: 'https://4A32B32DC672DC56BADDD48EC3901D90.gr7.us-east-1.eks.amazonaws.com') {
                    sh 'kubectl apply -f deploy-svc.yml'
                    sleep 60
                }
            }
        }
        stage ('verify deploy to eks') {
            steps {
                withKubeConfig(caCertificate: '', clusterName: 'my-eks', contextName: '', credentialsId: 'k8s-secret', namespace: 'webapps', restrictKubeConfigAccess: false, serverUrl: 'https://4A32B32DC672DC56BADDD48EC3901D90.gr7.us-east-1.eks.amazonaws.com') {
                    sh 'kubectl get svc'
                }
            }
        }
    }
}