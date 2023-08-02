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
                git url: '${git-url}',
                    branch: '${git-branch}',
                    credentialsId: '${githubKey}'
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
                script {
                    ${build-docker}
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
                    ${push-docker}
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
                    ${deploy}
                }
            }
        }
    }
}

// pr test
