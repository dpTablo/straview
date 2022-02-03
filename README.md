# straview
straview는 strava 데이터를 효과적으로 보기 위한 서비스 입니다.

## springdoc
springdoc Open API를 이용하여 API 명세를 제공합니다.

## Database PostgreSQL
DB로 `PostgreSQL`를 사용하였고, docker hub의 [postgres](https://hub.docker.com/_/postgres) docker image 를 이용하였습니다.
기본적인 테스트는 아래와 같이 docker 컨테이너를 생성하여 테스트할 수 있습니다.
```bash
docker pull postgres
docker run -d -p 5432:5432 -e POSTGRES_PASSWORD="straview%Pass%word%" --name PostgreSQL01 postgres
```