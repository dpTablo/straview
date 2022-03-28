# straview
straview는 strava의 라이딩 데이터를 효과적으로 보기 위한 서비스 입니다. [Strava](https://www.strava.com) 운동기록에 대한 빠른 조회와 지원되지 않는 데이터의 시각화를 제공합니다.
straview 저장소는 straview 서비스의 back-end 구현코드이며, 추후 front-end 의 구현이 진행되는데로 추가될 예정입니다.

## Strava API
straview는 [Strava API](https://developers.strava.com/) 를 이용하여 데이터를 가져옵니다. 
Strava에서 [내 API 애플리케이션](https://www.strava.com/settings/api) 페이지에서 설정한 정보들이 인증에 필요합니다. [app.properties 설정](#appproperties-설정) 에 strava 설정 항목에 인증정보를 설정해야 어플리케이션이 올바르게 구동됩니다.   

자세한 내용은 [Strava API](https://developers.strava.com/) 를 참조해 주십시오.

## springdoc
springdoc Open API를 이용하여 API 명세를 제공합니다.

서버를 기동한 후 `http://{host}/{context}/swagger-ui/index.html` url로 확인할 수 있습니다. 

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

## macos 에서 실행할 때 오류에 관하여
macos, ARM 계열(Apple M1)의 cpu 에서 straview 를 실행하면 아래 오류가 발생할 수 있습니다.
스프링 클라우드 게이트웨이 사용 시 발생합니다.
```bash
2022-03-10 19:00:13.763 ERROR 52672 --- [nio-8080-exec-3] i.n.r.d.DnsServerAddressStreamProviders  : Unable to load io.netty.resolver.dns.macos.MacOSDnsServerAddressStreamProvider, fallback to system defaults. This may result in incorrect DNS resolutions on MacOS.

java.lang.reflect.InvocationTargetException: null
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_302]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_302]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_302]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:423) ~[na:1.8.0_302]
	at io.netty.resolver.dns.DnsServerAddressStreamProviders.<clinit>(DnsServerAddressStreamProviders.java:64) ~[netty-resolver-dns-4.1.73.Final.jar:4.1.73.Final]
	at io.netty.resolver.dns.DnsNameResolverBuilder.<init>(DnsNameResolverBuilder.java:60) [netty-resolver-dns-4.1.73.Final.jar:4.1.73.Final]
	at reactor.netty.transport.NameResolverProvider.newNameResolverGroup(NameResolverProvider.java:479) [reactor-netty-core-1.0.15.jar:1.0.15]
	at reactor.netty.tcp.TcpResources.getOrCreateDefaultResolver(TcpResources.java:315) [reactor-netty-core-1.0.15.jar:1.0.15]
	at reactor.netty.http.HttpResources.getOrCreateDefaultResolver(HttpResources.java:139) [reactor-netty-http-1.0.15.jar:1.0.15]
	at reactor.netty.http.client.HttpClientConfig.defaultAddressResolverGroup(HttpClientConfig.java:382) [reactor-netty-http-1.0.15.jar:1.0.15]
```

라이브러리 `io.netty:netty-resolver-dns-native-macos:4.1.73.Final` 가 의존성에 의해 참조되어 있으며, 기본적으로 x86_64 의존성만 추가되어 있습니다.

build.gradle.kts 에 아래 의존성을 추가하면 발생하지 않습니다.
```kotlin
implementation("io.netty:netty-resolver-dns-native-macos:4.1.73.Final:osx-aarch_64")
```
일반적인 intel, amd 등의 x86 계열 아키텍처에서는 발생하지 않습니다.