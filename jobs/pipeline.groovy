pipeline {
    agent any
    parameters {
        choice(name: 'ENV', choices: ['dev', 'stage', 'prod'], description: 'Select Deployment Environment')
        choice(name: 'ACTION', choices: ['DEPLOY', 'ROLLBACK', 'DELETE'], description: 'Choose Deployment Action')
        string(name: 'APP_NAME', defaultValue: 'my-app', description: 'Enter Application Name')
    }
    environment {
        KUBECONFIG = "$HOME/.kube/config"  // Path to kubeconfig file
        NAMESPACE = " (params.ENV == 'dev') ? 'dev' : 'stage' "
        VALUES_FILE = "helm-chart/values-${params.ENV.toLowerCase()}.yaml"
    }
    stages {
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    if (params.ACTION == "DEPLOY") {
                        echo "Deploying ${params.APP_NAME} to ${params.APP_NAME} using Helm"
                        sh 'ls'
                        // sh """
                        // helm upgrade --install ${params.APP_NAME} ./helm-chart/ \
                        // --namespace ${env.NAMESPACE} \
                        // --values ${env.VALUES_FILE}
                        // """
                    } // else if (params.ACTION == "ROLLBACK") {
                    //     echo "Rolling back ${params.APP_NAME} in ${params.ENV}"
                    //     sh "helm rollback ${params.APP_NAME} --namespace ${env.NAMESPACE}"
                    // } else if (params.ACTION == "DELETE") {
                    //     echo "Deleting ${params.APP_NAME} in ${params.ENV}"
                    //     sh "helm uninstall ${params.APP_NAME} --namespace ${env.NAMESPACE}"
                    // } 
                    else {
                        error("Invalid action selected!")
                    }
                }
            }
        }
    }
}
