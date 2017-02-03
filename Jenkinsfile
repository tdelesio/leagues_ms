node {
  // Mark the code checkout 'stage'....
  stage 'Checkout'
  // Get some code from a GitHub repository
  git url: 'https://github.com/tdelesio/leagues_ms/'
  // Clean any locally modified files and ensure we are actually on origin/master
  // as a failed release could leave the local workspace ahead of origin/master
  sh "git clean -f && git reset --hard origin/feature-myp-12"
  def mvnHome = tool 'maven'
  echo mvnHome
  // we want to pick up the version from the pom
 // def pom = readMavenPom file: '/server-eureka/pom.xml'
 //  def version = pom.version.replace("-SNAPSHOT", ".${currentBuild.number}")
  // Mark the code build 'stage'....
  stage 'Build'
  
  echo "sh pwd"
  
  sh pwd
  // Run the maven build this is a release that keeps the development version 
  // unchanged and uses Jenkins to provide the version number uniqueness
  sh "${mvnHome}/bin/mvn clean intall /server-eureka/pom.xml"
  // Now we have a step to decide if we should publish to production 
  // (we just use a simple publish step here)
}
