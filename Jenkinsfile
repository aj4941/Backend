pipeline {
    agent any
    triggers {
        pollSCM('*/3 * * * *')
    }
    environment {
        imageName = "aj4941/morandi-server"
        registryCredential = 'morandi-docker-key'
        dockerImage = ''
    }
    stages {
        stage('Build Gradle') {
            steps {
                echo 'Build Gradle'
                sh './gradlew clean bootJar'
            }
            post {
                failure {
                    error 'This pipeline stops here..'
                }
            }
        }
        stage('Build Docker') {
            steps {
                echo 'Build Docker'
                script {
                    dockerImage = docker.build imageName
                }
            }
            post {
                failure {
                    error 'This pipeline stops here..'
                }
            }
        }
        stage('Push Docker') {
            steps {
                echo 'Push Docker'
                script {
                    docker.withRegistry('', registryCredential) {
                        dockerImage.push("latest")
                    }
                }
            }
            post {
                failure {
                    error 'This pipeline stops here..'
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'SSH'
                script {
                    def imageExists = sh(returnStdout: true, script: "docker images -q ${imagename}").trim()
                    if (imageExists) {
                        sh "docker rmi ${imagename}"
                    }
                    sshagent(['morandi-ubuntu-key']) {
                        sh "ssh -o StrictHostKeyChecking=no ubuntu@10.0.11.225 'docker stop morandi-container || true'"
                        sh "ssh -o StrictHostKeyChecking=no ubuntu@10.0.11.225 'docker rm morandi-container || true'"
                        sh "ssh -o StrictHostKeyChecking=no ubuntu@10.0.11.225 'docker rmi morandi-server || true'"
                        sh "ssh -o StrictHostKeyChecking=no ubuntu@10.0.11.225 'docker pull ${imagename}'"
                        sh "ssh -o StrictHostKeyChecking=no ubuntu@10.0.11.225 'docker run -d -p 8080:8080 —name morandi-container ${imageName}'"
                    }
                }
            }
        }
    }
}