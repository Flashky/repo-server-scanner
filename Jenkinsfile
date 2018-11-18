pipeline {
	agent any
	tools {
		maven 'M3_Jenkins' 
	}
	stages {
		
		stage('Clone Repository') {
			steps {
				// Get some code from a GitHub repository
				git 'https://github.com/Flashky/repo-server-scanner.git'
			}

		}

		stage('Build') {
			steps {
				sh 'mvn --version'
				sh 'mvn -f server-scanner/pom.xml install'
			}
		}
	}
}