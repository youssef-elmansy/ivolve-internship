def call(String imagename, String tag) {
sh "docker build -t ${imagename}:${tag} ."
}
