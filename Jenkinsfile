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
       stage("Deploy with docker compose") {
    steps {
        withCredentials([
            string(credentialsId: 'backend-prod-DEV_POSTGRES_DOCKER_URL', variable: 'DEV_POSTGRES_DOCKER_URL'),
            string(credentialsId: 'backend-prod-DEV_POSTGRES_USERNAME', variable: 'DEV_POSTGRES_USERNAME'),
            string(credentialsId: 'backend-prod-DEV_POSTGRES_PASSWORD', variable: 'DEV_POSTGRES_PASSWORD'),

            string(credentialsId: 'backend-prod-SENDGRID_API_KEY', variable: 'SENDGRID_API_KEY'),
            string(credentialsId: 'backend-prod-SENDGRID_SENDER_EMAIL', variable: 'SENDGRID_SENDER_EMAIL'),

            string(credentialsId: 'backend-prod-JWT_SECRET_KEY', variable: 'JWT_SECRET'),
            string(credentialsId: 'backend-prod-JWT_EXPIRATION_MINUTES', variable: 'JWT_EXPIRATION_MINUTES'),
            string(credentialsId: 'backend-prod-JWT_EXPIRATION_DAYS', variable: 'JWT_EXPIRATION_DAYS'),
            string(credentialsId: 'backend-prod-JWT_ACCESS_SECRET_KEY', variable: 'JWT_ACCESS_SECRET_KEY'),
            string(credentialsId: 'backend-prod-JWT_REFRESH_SECRET_KEY', variable: 'JWT_REFRESH_SECRET_KEY'),

            string(credentialsId: 'backend-prod-ZIPKIN_DOCKER_HOST', variable: 'ZIPKIN_DOCKER_HOST'),
            string(credentialsId: 'backend-prod-LOGSTASH_DOCKER_HOST', variable: 'LOGSTASH_DOCKER_HOST'),
            string(credentialsId: 'backend-prod-PROMETHEUS_HOST', variable: 'PROMETHEUS_HOST'),

            string(credentialsId: 'backend-prod-GF_SECURITY_ADMIN_USER', variable: 'GF_SECURITY_ADMIN_USER'),
            string(credentialsId: 'backend-prod-GF_SECURITY_ADMIN_PASSWORD', variable: 'GF_SECURITY_ADMIN_PASSWORD'),

            string(credentialsId: 'backend-prod-DEV_SERVER_PORT', variable: 'DEV_SERVER_PORT'),
            string(credentialsId: 'backend-prod-ZIPKIN_PORT', variable: 'ZIPKIN_PORT'),
            string(credentialsId: 'backend-prod-KIBANA_PORT', variable: 'KIBANA_PORT'),
            string(credentialsId: 'backend-prod-PROMETHEUS_PORT', variable: 'PROMETHEUS_PORT'),
            string(credentialsId: 'backend-prod-GRAFANA_PORT', variable: 'GRAFANA_PORT'),
            string(credentialsId: 'backend-prod-BACKEND_PORT', variable: 'BACKEND_PORT')
        ]) {
            withEnv([
                "IMAGE_TAG=${env.IMAGE_TAG}"
            ]) {
                bat """
                    @echo off
                    echo Checking env variables in Windows bat...

                    if defined DEV_POSTGRES_DOCKER_URL (echo DEV_POSTGRES_DOCKER_URL=OK) else (echo DEV_POSTGRES_DOCKER_URL=MISSING)
                    if defined DEV_POSTGRES_USERNAME (echo DEV_POSTGRES_USERNAME=OK) else (echo DEV_POSTGRES_USERNAME=MISSING)
                    if defined DEV_POSTGRES_PASSWORD (echo DEV_POSTGRES_PASSWORD=OK) else (echo DEV_POSTGRES_PASSWORD=MISSING)

                    if defined SENDGRID_API_KEY (echo SENDGRID_API_KEY=OK) else (echo SENDGRID_API_KEY=MISSING)
                    if defined SENDGRID_SENDER_EMAIL (echo SENDGRID_SENDER_EMAIL=OK) else (echo SENDGRID_SENDER_EMAIL=MISSING)

                    if defined JWT_SECRET (echo JWT_SECRET=OK) else (echo JWT_SECRET=MISSING)
                    if defined JWT_ACCESS_SECRET_KEY (echo JWT_ACCESS_SECRET_KEY=OK) else (echo JWT_ACCESS_SECRET_KEY=MISSING)
                    if defined JWT_REFRESH_SECRET_KEY (echo JWT_REFRESH_SECRET_KEY=OK) else (echo JWT_REFRESH_SECRET_KEY=MISSING)

                    if defined ZIPKIN_DOCKER_HOST (echo ZIPKIN_DOCKER_HOST=OK) else (echo ZIPKIN_DOCKER_HOST=MISSING)
                    if defined LOGSTASH_DOCKER_HOST (echo LOGSTASH_DOCKER_HOST=OK) else (echo LOGSTASH_DOCKER_HOST=MISSING)
                    if defined PROMETHEUS_HOST (echo PROMETHEUS_HOST=OK) else (echo PROMETHEUS_HOST=MISSING)

                    if defined KIBANA_PORT (echo KIBANA_PORT=OK) else (echo KIBANA_PORT=MISSING)
                    if defined ZIPKIN_PORT (echo ZIPKIN_PORT=OK) else (echo ZIPKIN_PORT=MISSING)
                    if defined PROMETHEUS_PORT (echo PROMETHEUS_PORT=OK) else (echo PROMETHEUS_PORT=MISSING)
                    if defined GRAFANA_PORT (echo GRAFANA_PORT=OK) else (echo GRAFANA_PORT=MISSING)
                    if defined GF_SECURITY_ADMIN_USER (echo GF_SECURITY_ADMIN_USER=OK) else (echo GF_SECURITY_ADMIN_USER=MISSING)
                    if defined GF_SECURITY_ADMIN_PASSWORD (echo GF_SECURITY_ADMIN_PASSWORD=OK) else (echo GF_SECURITY_ADMIN_PASSWORD=MISSING)

                    if defined BACKEND_PORT (echo BACKEND_PORT=OK) else (echo BACKEND_PORT=MISSING)
                    if defined IMAGE_TAG (echo IMAGE_TAG=OK) else (echo IMAGE_TAG=MISSING)

                    echo Validating docker compose...
                    docker compose config --quiet

                    echo Pulling images...
                    docker compose pull

                    echo Starting containers...
                    docker compose up -d
                """
            }
        }
    }
}
    }
}
