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
                git credentialsId: 'gitcred', url: 'https://github.com/Deepthinker07/bg-eks.git'
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
        stage ('trivy filesystem scan') {
            steps {
                sh 'trivy fs --format table -o trivy-fs-report .'
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
        stage ('quality gates') {
            steps {
                timeout(time: 1, unit: 'MINUTES') {
                waitForQualityGate abortPipeline: true
               }
            }
        }
        stage ('code package') {
            steps {
                sh 'mvn package'
            }
        }
        stage ('push artifact to nexus') {
            steps {
                nexusArtifactUploader artifacts:
                 [[artifactId: 'database_service_project',
                  classifier: '',
                   file: 'target/database_service_project-0.0.4.jar',
                    type: 'jar']],
                     credentialsId: 'nexuscred',
                      groupId: 'com.javaproject',
                       nexusUrl: '3.83.172.142:8081',
                        nexusVersion: 'nexus3',
                         protocol: 'http',
                          repository: 'artifact',
                           version: '0.0.4+$BUILD_NUMBER'
            }
        }
        stage ('docker image build') {
            steps {
                sh 'docker build -t $userName/$imgName .'
            }
        }
        stage ('trivy image scan') {
            steps {
                sh 'trivy image --format table -o trivy-image-scan $userName/$imgName'
            }
        }
        stage ('push image to dockerhub') {
            steps {
                withDockerRegistry(credentialsId: 'dockercred', url: '') {
                    sh 'docker push $userName/$imgName'
                }    
            }
        }
    }
}