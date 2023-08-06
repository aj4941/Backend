pipeline {
    agent any

    environment {
        registryCredential = 'docker-key'
        dockerImage = ''
    }

    stages {
        stage('Change File') {
            steps {
                script {
                    def applicationYmlPath = './src/main/resources/application.yml'
                    def oAuthConstantsPath = './src/main/java/swm_nm/morandi/auth/constants/OAuthConstants.java'
                    if (fileExists(applicationYmlPath)) {
                        sh "rm ${applicationYmlPath}"
                    }
                    if (fileExists(oAuthConstantsPath)) {
                        sh "rm ${oAuthConstantsPath}"
                    }
                    sh "cp /var/jenkins_home/application.yml ${applicationYmlPath}"
                    sh "cp /var/jenkins_home/OAuthConstants.java ${oAuthConstantsPath}"
                }
            }
        }
        stage('Bulid Gradle') {
            steps {
                echo 'Bulid Gradle'
                sh './gradlew clean bootJar'
            }
            post {
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }
        stage('Bulid Docker') {
            steps {
                echo 'Bulid Docker'
                script {
                    dockerImage = docker.build "${image}"
                }
            }
            post {
                failure {
                    error 'This pipeline stops here...'
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
                    error 'This pipeline stops here...'
                }
            }
        }
        stage('Deploy') {
          steps {
              echo 'SSH'
              script {
                  def imageExists = sh(returnStdout: true, script: "docker images -q ${image}").trim()
                  if (imageExists) {
                      sh "docker rmi ${image}"
                  }
                  sshagent(['server-key']) {
                      sh "ssh -o StrictHostKeyChecking=no ubuntu@10.0.11.225 'sudo docker stop morandi-container || true'"
                      sh "ssh -o StrictHostKeyChecking=no ubuntu@10.0.11.225 'sudo docker rm morandi-container || true'"
                      sh "ssh -o StrictHostKeyChecking=no ubuntu@10.0.11.225 'sudo docker rmi aj4941/morandi-server || true'"
                      sh "ssh -o StrictHostKeyChecking=no ubuntu@10.0.11.225 'sudo docker pull aj4941/morandi-server'"
                      sh "ssh -o StrictHostKeyChecking=no ubuntu@10.0.11.225 'sudo docker run -d -p 8080:8080 --name morandi-container aj4941/morandi-server'"
                  }
              }
          }
       }
   }
}