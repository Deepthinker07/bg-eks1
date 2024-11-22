@Library{'edhana'} _
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
                git_cred.groovy()
            }
        }
        stage ('compile & test') {
            parallel {
                stage ('compile') {
                    steps {
                        sh 'mvn compile'
                    }
                }    
                stage ('unit test') {
                    steps {
                        sh 'mvn test'
                    }
                }
            }    
        }
        stage ('static code analysis') {
            steps {
                sonar.groovy()
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
                withDockerRegistry(credentialsId: 's1', url: '') {
                    sh 'docker push $userName/$imgName'
                }    
            }
        }
        stage ('deploy to eks') {
            steps {
                withKubeConfig(caCertificate: '', clusterName: ' singh-EKS', contextName: '', credentialsId: 'kube', namespace: 'webapps', restrictKubeConfigAccess: false, serverUrl: 'https://883E66BF913871535B678866B4C102FF.gr7.ap-southeast-1.eks.amazonaws.com') {
                    sh 'kubectl apply -f deploy-svc.yml'
                    sleep 60
                }
            }
        }
        stage ('cerify to eks') {
            steps {
                withKubeConfig(caCertificate: '', clusterName: ' singh-EKS', contextName: '', credentialsId: 'kube', namespace: 'webapps', restrictKubeConfigAccess: false, serverUrl: 'https://883E66BF913871535B678866B4C102FF.gr7.ap-southeast-1.eks.amazonaws.com') {
                    sh 'kubectl get svc'
                }
            }
        }
            
    }  
}
// skip, parallel, slack & post 