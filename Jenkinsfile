pipeline {
  agent { label 'docker' }

  options {
    timestamps()
    ansiColor('xterm')
    skipDefaultCheckout true
  }

  environment {
    MVN_IMAGE = 'maven:3.9-eclipse-temurin-21'
    APP_IMAGE = "spring-boot-practice:${env.BUILD_NUMBER}"
  }

  stages {
    stage('Checkout') {
      steps {
        cleanWs()
        checkout scm
      }
    }

    stage('Build + Test') {
      steps {
        script {
          docker.image(env.MVN_IMAGE).inside('-e HOME=${env.WORKSPACE} -v /home/jenkins/.m2:${env.WORKSPACE}/.m2') {
            sh 'id'
            sh 'echo "HOME=$HOME"'
            sh 'ls -ld $HOME $HOME/.m2'
            sh 'java --version'
            sh 'chmod +x mvnw'
            sh './mvnw -v'
            sh './mvnw -B clean test'
          }
        }
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Package (inside Docker)') {
      steps {
        script {
          docker.image(env.MVN_IMAGE).inside('-e HOME=${env.WORKSPACE} -v /home/jenkins/.m2:${env.WORKSPACE}/.m2') {
            sh './mvnw -B -DskipTests package'
            sh 'ls -la target || true'
          }
        }
      }
      post {
        success {
          archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
      }
    }

    stage('Build Docker Image') {
      steps {
        // Build without Dockerfile (SpringBoot buildpacks) - simpler but less control and heavier image
        // sh './mvnw -B -DskipTests spring-boot:build-image -Dspring-boot.build-image.imageName=$APP_IMAGE'
        sh 'docker build -t ${APP_IMAGE} .'
        sh 'docker images | head'
      }
    }
  }
}
