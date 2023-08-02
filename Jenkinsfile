pipeline {
    agent any

    triggers {
        pollSCM('*/3 * * * *')
    }

    environment {
        registryCredential = 'docker-key'
        dockerImage = ''
    }

    stages {
        // git에서 repository clone
        stage('Prepare') {
          steps {
            echo 'Clonning Repository'
            git url: 'https://github.com/SWM-NM/morandi-server',
                branch: 'master',
                credentialsId: 'github-personal-access-token'
            }
            post {
             success {
               echo 'Successfully Cloned Repository'
             }
           	 failure {
               error 'This pipeline stops here...'
             }
          }
        }

        // gradle build
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

        // docker build
        stage('Bulid Docker') {
          steps {
            echo 'Bulid Docker'
            script {
                dockerImage = docker.build imageName
            }
          }
          post {
            failure {
              error 'This pipeline stops here...'
            }
          }
        }

        // docker push
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
                    sh "ssh -o StrictHostKeyChecking=no ${server-ip} 'docker stop ${container} || true'"
                    sh "ssh -o StrictHostKeyChecking=no ${server-ip} 'docker rm ${container} || true'"
                    sh "ssh -o StrictHostKeyChecking=no ${server-ip} 'docker rmi ${container} || true'"
                    sh "ssh -o StrictHostKeyChecking=no ${server-ip} 'docker pull ${image}'"
                    sh "ssh -o StrictHostKeyChecking=no ${server-ip} 'docker run -d -p 8080:8080 —name ${container} ${imageName}'"
                }
            }
          }
       }
    }
}