pipeline{
    agent any

    tools{
        maven "MAVEN"
    }

    environment{
        APP_NAME = 'backend-service'
        IMAGE_TAG = 'latest'
    }

    stages{
        stage("Clean workspace"){
            steps{
                cleanWs()
            }
        }
        stage("Checkout from SCM"){
            steps{
                git branch:"main",credentialsId:"Github", url: "https://github.com/vanquang239dn/springboot-backend"
            }
        }
        stage("Test Application"){
            steps{
                withCredentials([
                    string(credentialsId: 'backend-prod-DEV_POSTGRES_URL', variable: 'DEV_POSTGRES_URL'),
                    string(credentialsId: 'backend-prod-DEV_POSTGRES_USERNAME', variable: 'DEV_POSTGRES_USERNAME'),
                    string(credentialsId: 'backend-prod-DEV_POSTGRES_PASSWORD', variable: 'DEV_POSTGRES_PASSWORD'),
                    string(credentialsId: 'backend-prod-DEV_SERVER_PORT', variable: 'DEV_SERVER_PORT'),
                    string(credentialsId: 'backend-prod-SENDGRID_API_KEY', variable: 'SENDGRID_API_KEY'),
                    string(credentialsId: 'backend-prod-SENDGRID_SENDER_EMAIL', variable: 'SENDGRID_SENDER_EMAIL'),
                    string(credentialsId: 'backend-prod-JWT_SECRET_KEY', variable: 'JWT_SECRET'),
                    string(credentialsId: 'backend-prod-JWT_EXPIRATION_MINUTES', variable: 'JWT_EXPIRATION_MINUTES'),
                    string(credentialsId: 'backend-prod-JWT_EXPIRATION_DAYS', variable: 'JWT_EXPIRATION_DAYS'),
                    string(credentialsId: 'backend-prod-JWT_ACCESS_SECRET_KEY', variable: 'JWT_ACCESS_SECRET_KEY'),
                    string(credentialsId: 'backend-prod-JWT_REFRESH_SECRET_KEY', variable: 'JWT_REFRESH_SECRET_KEY')
                ]){
                    script {
                        if (isUnix()) {
                            sh "./mvnw test -Dspring.profiles.active=dev"
                        } else {
                            bat "mvnw.cmd test -Dspring.profiles.active=dev"
                        }
                    }
                }
            }
        }
        stage("Build image with Jib") {
            steps {
                script {
                    if (isUnix()) {
                        sh "./mvnw clean package jib:build -DskipTests"
                    } else {
                        bat "mvnw.cmd clean package jib:build -DskipTests"
                    }
                }
            }
        }
        stage("Build Dockerfile with Compose") {
            steps {
                withCredentials([
                    string(credentialsId: 'backend-prod-ZIPKIN_HOST', variable: 'ZIPKIN_HOST'),
                    string(credentialsId: 'backend-prod-ZIPKIN_DOCKER_HOST', variable: 'ZIPKIN_DOCKER_HOST'),
                    string(credentialsId: 'backend-prod-LOGSTASH_HOST', variable: 'LOGSTASH_HOST'),
                    string(credentialsId: 'backend-prod-LOGSTASH_MONITORING_HOST', variable: 'LOGSTASH_MONITORING_HOST'),
                    string(credentialsId: 'backend-prod-LOGSTASH_DOCKER_HOST', variable: 'LOGSTASH_DOCKER_HOST'),
                    string(credentialsId: 'backend-prod-ELASTICSEARCH_HOST', variable: 'ELASTICSEARCH_HOST'),
                    string(credentialsId: 'backend-prod-ELASTICSEARCH_DOCKER_HOST', variable: 'ELASTICSEARCH_DOCKER_HOST'),
                    string(credentialsId: 'backend-prod-PROMETHEUS_HOST', variable: 'PROMETHEUS_HOST'),
                    string(credentialsId: 'backend-prod-PROMETHEUS_DOCKER_HOST', variable: 'PROMETHEUS_DOCKER_HOST'),
                    string(credentialsId: 'backend-prod-GF_SECURITY_ADMIN_USER', variable: 'GF_SECURITY_ADMIN_USER'),
                    string(credentialsId: 'backend-prod-GF_SECURITY_ADMIN_PASSWORD', variable: 'GF_SECURITY_ADMIN_PASSWORD'),
                    string(credentialsId: 'backend-prod-ZIPKIN_PORT', variable: 'ZIPKIN_PORT'),
                    string(credentialsId: 'backend-prod-KIBANA_PORT', variable: 'KIBANA_PORT'),
                    string(credentialsId: 'backend-prod-PROMETHEUS_PORT', variable: 'PROMETHEUS_PORT'),
                    string(credentialsId: 'backend-prod-GRAFANA_PORT', variable: 'GRAFANA_PORT')
                ]){
                    script {
                        if (isUnix()) {
                            sh "docker compose up -d"
                        } else {
                            bat "docker compose up -d"
                        }
                    }
                }
            }
        }
    }
}
