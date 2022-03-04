# straview
straview는 strava의 라이딩 데이터를 효과적으로 보기 위한 서비스 입니다. [Strava](https://www.strava.com) 운동기록에 대한 빠른 조회와 지원되지 않는 데이터의 시각화를 제공합니다.
straview 저장소는 straview 서비스의 back-end 구현코드이며, 추후 front-end 의 구현이 진행되는데로 추가될 예정입니다.

## Strava API
straview는 [Strava API](https://developers.strava.com/) 를 이용하여 데이터를 가져옵니다. 
Strava에서 [내 API 애플리케이션](https://www.strava.com/settings/api) 페이지에서 설정한 정보들이 인증에 필요합니다. [app.properties 설정](#appproperties-설정) 에 strava 설정 항목에 인증정보를 설정해야 어플리케이션이 올바르게 구동됩니다.   

자세한 내용은 [Strava API](https://developers.strava.com/) 를 참조해 주십시오.

## springdoc
springdoc Open API를 이용하여 API 명세를 제공합니다.

## Database PostgreSQL
DB로 `PostgreSQL`를 사용하였고, docker hub의 [postgres](https://hub.docker.com/_/postgres) docker image 를 이용하였습니다.
기본적인 테스트는 아래와 같이 docker 컨테이너를 생성하여 테스트할 수 있습니다.
```bash
docker pull postgres
docker run -d -p 5432:5432 -e POSTGRES_PASSWORD="straview%Pass%word%" --name PostgreSQL01 postgres
```

## app.properties 설정
`src/main/resources/app.properties` 파일에 어플리케이션에서 사용되는 속성값을 설정할 수 있습니다.

### jwt 관련 설정
- jwt.privateKey : jwt 토큰 생성 시 사용되는 private key 값
- jwt.expiryMinutes : jwt 토큰의 만료시간 설정(분)
- jwt.issuer : jwt 토큰 생성 시 사용되는 issuer 값

### strava 관련 설정
`strava.auth.redirectUrl` 속성은 strava oauth2 인증 후 리다이렉트 되는 url 경로이며, straview 개발자가 변경하는 값으로 사용자가 변경할 경우 인증에 문제가 발생할 수 있습니다.

`strava.api`로 시작되는 속성값은 [Strava API](https://developers.strava.com/)의 api 요청 url 입니다. 사용자가 변경할 필요가 없습니다. 

- strava.clientId : Strava API 를 요청할 때 필요한 클라이언트 id   
- strava.clientSecretFilePath : 클라이언트 id에 대한 암호값이 저장된 파일 경로