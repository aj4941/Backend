pipeline {
    agent any
    triggers {
        pollSCM('*/3 * * * *')
    }
    environment {
        imageName = "${imageName}"
        registryCredential = '${dockerKey}'
        dockerImage = ''
    }
    stages {
        stage('Prepare') {
            steps {
                echo 'Clonning Repository'
                git url: 'https://github.com/SWM-NM/morandi-backend',
                    branch: 'master',
                    credentialsId: 'github_personal_access_token'
            }
            post {
                failure {
                    error 'This pipeline stops here..'
                }
            }
        }
        stage('Build Gradle') {
            steps {
                echo 'Build Gradle'
                sh '${Build}'
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
                sh '${build-docker}'
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
                sh '${push-docker}'
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
                    def imageExists = sh(returnStdout: true, script: "docker images -q ${imageName}").trim()
                    if (imageExists) {
                        sh "docker rmi ${imagename}"
                    }
                    sshagent(["${serverKey}"]) {
                        sh "ssh -o StrictHostKeyChecking=no ${serverIp} 'docker stop ${containerName} || true'"
                        sh "ssh -o StrictHostKeyChecking=no ${serverIp} 'docker rm ${containerName} || true'"
                        sh "ssh -o StrictHostKeyChecking=no ${serverIp} 'docker rmi ${image} || true'"
                        sh "ssh -o StrictHostKeyChecking=no ${serverIp} 'docker pull ${imageName}'"
                        sh "ssh -o StrictHostKeyChecking=no ${serverIp} 'docker run -d -p 8080:8080 —name ${containerName} ${imageName}'"
                    }
                }
            }
        }
    }
}
