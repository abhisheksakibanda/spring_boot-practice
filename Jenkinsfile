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
    MVN_ARGS = "-e HOME=\$WORKSPACE -v /home/jenkins/.m2:\$WORKSPACE/.m2"
  }

  stages {
    stage('Checkout') {
      steps {
        cleanWs()
        checkout scm
      }
    }

    stage('Get Git Info') {
      steps {
        script {
          env.GIT_COMMIT = sh(script: "git rev-parse --short HEAD", stdout: true).trim()
        }
      }
    }

    stage('Build + Test') {
      steps {
        script {
          docker.image(env.MVN_IMAGE).inside(env.MVN_ARGS) {
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
          docker.image(env.MVN_IMAGE).inside(env.MVN_ARGS) {
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
        def imageTag = "${env.BUILD_NUMBER}-${env.GIT_COMMIT}"
        // Build without Dockerfile (SpringBoot buildpacks) - simpler but less control and heavier image
        // sh './mvnw -B -DskipTests spring-boot:build-image -Dspring-boot.build-image.imageName=$APP_IMAGE'
        sh "docker build -t ${imageTag} ."
        sh 'docker images | head'
      }
    }
  }
}
