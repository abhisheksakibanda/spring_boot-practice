pipeline {
  agent { label 'docker' }

  options {
    timestamps()
    ansiColor('xterm')
  }

  environment {
    MVN_IMAGE = 'maven:3.9-eclipse-temurin-21'
    APP_IMAGE = "spring-boot-practice:${env.BUILD_NUMBER}"
  }

  stages {
    stage('Build + Test') {
      steps {
        script {
          docker.image(env.MVN_IMAGE).inside('-e HOME=$WORKSPACE') {
            sh 'java --version'
            sh 'mkdir -p $HOME/.m2'
            sh 'ls -la mvnw'
            sh 'chmod +x mvnw'
            sh 'ls -la mvnw'
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
          docker.image(MVN_IMAGE).inside {
            sh './mnvw -B -DskipTests package'
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
        sh '.mnvw -B -DskipTests spring-boot:build-image -Dspring-boot.build-image.imageName=${APP_IMAGE}'
        sh 'docker images | head'
      }
    }
  }
}
