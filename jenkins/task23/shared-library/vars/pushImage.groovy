def call(String imageName, String tag) {
    withCredentials([usernamePassword(
        credentialsId: 'DockerHub',
        usernameVariable: 'USER',
        passwordVariable: 'PASS'
    )]) {
        sh '''
          echo $PASS | docker login -u $USER --password-stdin
          docker push ''' + imageName + ':' + tag
    }
}

