
# Content

[1. 기능 정의서](#기능-정의서)  
[2. 테이블 정의서](#테이블-정의서)  
[3. ERD](#erd-다이어그램)    
[4. 시스템 구성도](#시스템-구성도)  
[5. AWS 구성 내역 사진](#aws-구성-내역-사진)      
[6. AWS 내부 구축 내용](#aws-내부-구축-내용)
 




## 기능 정의서 
![기능정의서](https://github.com/pyjhoop/book-management/blob/main/outputs/%EA%B8%B0%EB%8A%A5%20%EC%A0%95%EC%9D%98%EC%84%9C.png)

## 테이블 정의서
![테이블 정의서](https://github.com/pyjhoop/book-management/blob/main/outputs/%ED%85%8C%EC%9D%B4%EB%B8%94%20%EC%A0%95%EC%9D%98%EC%84%9C_%EC%9D%B4%EB%AF%B8%EC%A7%80.png)

## ERD 다이어그램
![ERD](https://github.com/pyjhoop/book-management/blob/main/outputs/ERD.png)

## 시스템 구성도
![시스템 구성도](https://github.com/pyjhoop/book-management/blob/main/outputs/%EC%8B%9C%EC%8A%A4%ED%85%9C%20%EA%B5%AC%EC%84%B1%EB%8F%84.png)  

## AWS 구성 내역 사진

### EC2 내부구성
![EC2 내부구성](https://github.com/pyjhoop/book-management/blob/main/outputs/EC2%EB%82%B4%EB%B6%80%20%EA%B5%AC%EC%84%B1.png)    
    
### EC2 인바운드 설정
![EC2 인바운드 설정](https://github.com/pyjhoop/book-management/blob/main/outputs/EC2%EC%9D%B8%EB%B0%94%EC%9A%B4%EB%93%9C%20%EC%84%A4%EC%A0%95.png)        

### EC2 IAM 설정
![EC2 IAM 설정](https://github.com/pyjhoop/book-management/blob/main/outputs/EC2IAM%EC%9D%84%20%ED%86%B5%ED%95%9C%EC%97%AD%ED%95%A0%EB%B6%80%EC%97%AC.png)

### S3 설정
![S3 설정](https://github.com/pyjhoop/book-management/blob/main/outputs/S3%EB%B2%84%ED%82%B7%20%EC%86%8D%EC%84%B1.png)

### CodeDeploy 구성
![CodeDeploy 구성](https://github.com/pyjhoop/book-management/blob/main/outputs/CodeDeploy%20%EC%84%B8%EB%B6%80%EC%A0%95%EB%B3%B4.png)
        
## AWS 내부 구축 내용
### EC2 설정
1. EC2의 운영체제로 Ubuntu Server 20.04 LTS를 선택하고, EC2에 접속하기 위한 pem키를 생성합니다.
2. EC2를 재시작할 때마다 IP가 변경되는 것을 방지하기 위해 Elastic IP를 사용하여 고정 IP를 할당합니다.
3. SSH를 통해 EC2에 접속한 후, Java 11을 설치하고, CodeDeploy를 사용하기 위해 필요한 서비스를 다운로드합니다.
4. IAM에서 AmazonS3FullAccess와 AWSCodeDeployFullAccess를 설정하여 EC2에서 S3와 CodeDeploy 서비스를 사용할 수 있도록 합니다.

### S3 설정
1. 빌드된 파일을 보관하기 위한 S3 버킷을 생성합니다.
2. S3의 외부 접속을 통한 과금요소를 막고자 모든 퍼블릭 엑세스를 차단합니다.
3. S3에 접근할 수 있는 사용자를 등록하기 위해 IAM에서 사용자를 만들고 AmazonS3FullAccess, AWSCodeDeployFullAccess를 설정해줍니다.
4. Github Action에서 해당 사용자로 서비스에 접근하기 위해 IAM 사용자에서 엑세스 키와 시크릿키를 생성합니다.

### CodeDeploy 설정
1. CodeDeploy 생성후 권한을 추가하기 위해 IAM 에서 AWSCodeDeployRole 역할을 추가해줍니다.
2. 프로젝트 루트 디렉토리에 CodeDeploy가 배포를 관리하는데 사용할 appspec.yml파일을 작성해줍니다.

