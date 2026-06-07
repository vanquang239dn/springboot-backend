pipeline{
    agent any

    tools{
        maven "MAVEN"
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
                        bat "mvnw.cmd test -Dspring.profiles.active=dev"
                    }
                }
            }
        }

        stage("Prepare image tag") {
            steps {
                script {
                        env.IMAGE_TAG = bat(
                        script: "@git rev-parse --short=7 HEAD",
                        returnStdout: true
                        ).trim()

                        echo "IMAGE_TAG = ${env.IMAGE_TAG}"
                }
            }
        }

        stage("Build and Push image with Jib") {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'Dockerhub-credential',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    script {
                        bat """
                            mvnw.cmd clean package jib:build ^
                                -DskipTests ^
                                -Dimage.tag=%IMAGE_TAG% ^
                                -Djib.to.auth.username=%DOCKER_USERNAME% ^
                                -Djib.to.auth.password=%DOCKER_PASSWORD%
                        """
                    }
                }
            }
        }
        stage("Build Dockerfile with compose") {
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
                        bat """
                            docker compose pull
                            docker compose up -d
                        """
                    }
                }
            }
        }
    }
}
