//vars/mavne_build.groovy

def call(){

    node{

        checkout scm

        def testType

        stage("input"){
            def userInput = input message: 'Choose test type', parameters: [string(defaultValue: 'test', description: 'test or verify', name: 'testType', trim: false)], submitterParameter: 'store'
          testType = userInput.testType
        }

        stage('build'){
            withEnv(["PATH+MAVEN=${tool 'mvn-3.6.2'}/bin", 
            "JAVA_HOME=${tool  'openjdk8'}", 
            "PATH+JDK=${tool 'openjdk8'}/bin}"]){
                sh "ls -al $JAVA_HOME"
                sh 'mvn --version'
                sh "mvn clean ${params.testType} -Dmaven.test.failure.ignore=true" //keep going on by yello light
            }
        }
        stage('report') {
            junit 'target/surefire-reports/*.xml'
            jacoco execPattern: 'target/**.exec'
            addShortText background: '', borderColor: '', color: '', link: '', text: testType

        }
    }

}

