def call(String imagename,String tag) {

sh "trivy image ${imagename}:${tag} ||true"

}
