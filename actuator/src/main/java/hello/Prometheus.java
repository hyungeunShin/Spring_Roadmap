package hello;

public class Prometheus {
    /*
    프로메테우스
        애플리케이션에서 발생한 메트릭을 그 순간만 확인하는 것이 아니라 과거 이력까지 함께 확인하려면 메트릭을 보관하는 DB가 필요하다.
        이렇게 하려면 어디선가 메트릭을 지속해서 수집하고 DB에 저장해야 한다.
        프로메테우스가 바로 이런 역할을 담당한다.

    프로메테우스 설치
        https://prometheus.io/download/

    프로메테우스 실행
        prometheus.exe 실행 후 http://localhost:9090

    프로메테우스가 애플리케이션의 메트릭을 수집하도록 연동
        여기에는 2가지 작업이 필요하다.
            - 애플리케이션 설정: 프로메테우스가 애플리케이션의 메트릭을 가져갈 수 있도록 애플리케이션에서 프로메테우스 포멧에 맞추어 메트릭 만들기
            - 프로메테우스 설정: 프로메테우스가 우리 애플리케이션의 메트릭을 주기적으로 수집하도록 설정

        1. 애플리케이션 설정
            프로메테우스가 애플리케이션의 메트릭을 가져가려면 프로메테우스가 사용하는 포멧에 맞추어 메트릭을 만들어야 한다.
            참고로 프로메테우스는 /actuator/metrics 에서 보았던 포멧(JSON)은 이해하지 못한다.
            하지만 프로메테우스 포멧에 대한 부분은 걱정할 것이 없다. 마이크로미터가 이런 부분은 모두 해결해준다.
            각각의 메트릭들은 내부에서 마이크로미터 표준 방식으로 측정되고 있다. 따라서 어떤 구현체를 사용할지 지정만 해주면 된다.

            1) build.gradle -> implementation 'io.micrometer:micrometer-registry-prometheus'
                - 마이크로미터 프로메테우스 구현 라이브러리를 추가한다.
                - 이렇게 하면 스프링 부트와 액츄에이터가 자동으로 마이크로미터 프로메테우스 구현체를 등록해서 동작하도록 설정해준다.
                - 액츄에이터에 프로메테우스 메트릭 수집 엔드포인트가 자동으로 추가된다.
                    /actuator/prometheus

            2) http://localhost/actuator/prometheus
                모든 메트릭이 프로메테우스 포멧으로 만들어 진 것을 확인할 수 있다.

                /actuator/metrics 와 비교해서 프로메테우스에 맞추어 변환된 부분 몇 가지 확인(포멧 차이)
                    - jvm.info jvm_info : 프로메테우스는 . 대신에 _ 포멧을 사용한다. . 대신에 _ 포멧으로 변환된 것을 확인할 수 있다.
                    - logback.events logback_events_total : 로그수 처럼 지속해서 숫자가 증가하는 메트릭을 카운터라 한다. 프로메테우스는 카운터 메트릭의 마지막에는 관례상 _total 을 붙인다.
                    - http.server.requests 이 메트릭은 내부에 요청수, 시간 합, 최대 시간 정보를 가지고 있었다. 프로메테우스에서는 다음 3가지로 분리된다.
                        - http_server_requests_seconds_count : 요청 수
                        - http_server_requests_seconds_sum : 시간 합(요청수의 시간을 합함)
                        - http_server_requests_seconds_max : 최대 시간(가장 오래걸린 요청 수)

        2. 프로메테우스 - 수집 설정
            1) 프로메테우스 폴더에 있는 prometheus.yml 파일을 수정한다.
                <prometheus.yml>
                    global:
                      scrape_interval: 15s
                      evaluation_interval: 15s
    
                    alerting:
                      alertmanagers:
                        - static_configs:
                            - targets:
                              # - alertmanager:9093
    
                    rule_files:
    
                    scrape_configs:
                      - job_name: "prometheus"
                        static_configs:
                          - targets: ["localhost:9090"]
                      #추가
                      - job_name: "spring-actuator"          #job_name : 수집하는 이름이다. 임의의 이름을 사용하면 된다.
                        metrics_path: "/actuator/prometheus" #metrics_path : 수집할 경로를 지정한다.
                        scrape_interval: 1s                  #scrape_interval : 수집할 주기를 설정한다.
                        static_configs:
                          - targets: ["localhost:80"]        #targets : 수집할 서버의 IP, PORT를 지정한다.
                <prometheus.yml>
    
                scrape_interval 여기서는 예제를 빠르게 확인하기 위해서 수집 주기를 1s 로 했지만, 수집 주기의 기본 값은 1m 이다.
                수집 주기가 너무 짧으면 애플리케이션 성능에 영향을 줄 수 있으므로 운영에서는 10s ~ 1m 정도를 권장한다.
            
            2) 프로메테우스 연동 확인
                서버 재시작
                http://localhost:9090/config 에서 prometheus.yml 변경 내용 확인
                http://localhost:90990/targets 에서 prometheus.yml 변경 내용 확인
                
    프로메테우스 기능
        검색창에 http_server_requests_seconds_count 실행
        <결과>
        http_server_...{error="none", exception="none", instance="localhost:80", job="spring-actuator", method="GET", outcome="SUCCESS", status="200", uri="/log"}      7
        http_server_...{error="none", exception="none", instance="localhost:80", job="spring-actuator", method="GET", outcome="CLIENT_ERROR", status="404", uri="/**"}  1
            - 태그, 레이블: error, exception, instance, job, method, outcome, status, uri 는 각각 의 메트릭 정보를 구분해서 사용하기 위한 태그이다.
                          마이크로미터에서는 이것을 태그(Tag)라 하고, 프로메테우스에서는 레이블(Label)이라 한다.
            - 숫자: 끝에 마지막에 보면 7, 1 와 같은 숫자가 보인다. 이 숫자가 바로 해당 메트릭의 값이다.

        기본 기능
            Table -> Evaluation time 을 수정해서 과거 시간 조회 가능
            Graph -> 메트릭을 그래프로 조회 가능

        필터
            레이블을 기준으로 필터를 사용할 수 있다. 필터는 중괄호({}) 문법을 사용한다.

        레이블 일치 연산자
            - = 제공된 문자열과 정확히 동일한 레이블 선택
            - != 제공된 문자열과 같지 않은 레이블 선택
            - =~ 제공된 문자열과 정규식 일치하는 레이블 선택
            - !~ 제공된 문자열과 정규식 일치하지 않는 레이블 선택

        예)
            - uri=/log , method=GET 조건으로 필터
                - http_server_requests_seconds_count{uri="/log", method="GET"}
            - /actuator/prometheus 는 제외한 조건으로 필터
                - http_server_requests_seconds_count{uri!="/actuator/prometheus"}
            - method 가 GET , POST 인 경우를 포함해서 필터
                - http_server_requests_seconds_count{method=~"GET|POST"}
            - /actuator 로 시작하는 uri 는 제외한 조건으로 필터
                - http_server_requests_seconds_count{uri!~"/actuator.*"}

        연산자 쿼리와 함수
            - + (덧셈)
            - - (빼기)
            - * (곱셈)
            - / (분할)
            - % (모듈로)
            - ^ (승수/지수)
            - sum (값의 합계를 구한다.)
                예) sum(http_server_requests_seconds_count)
            - sum by (SQL 의 group by 기능과 유사)
                예) sum by(method, status)(http_server_requests_seconds_count)
            - count (메트릭 자체의 수 카운트)
                예) count(http_server_requests_seconds_count)
            - topk (상위 n개 메트릭 조회)
                예) topk(3, http_server_requests_seconds_count)
            - {metrics name} offset {time} (오프셋 수정자 - 현재를 기준으로 특정 과거 시점의 데이터를 반환)
                예) http_server_requests_seconds_count offset 10m -> 10분 전 http_server_requests_seconds_count 의 데이터
            - {metrics name}[{time}] (범위 벡터 선택기)
                예) http_server_requests_seconds_count[1m]
                마지막에 [1m] , [60s] 와 같이 표현한다. 지난 1분간의 모든 기록값을 선택한다.
                참고로 범위 벡터 선택기는 차트에 바로 표현할 수 없다. 범위 벡터 선택의 결과를 차트에 표현하기 위해서는 약간의 가공이 필요하다.
                데이터로는 확인할 수 있다.

    게이지와 카운터
        메트릭은 크게 보면 게이지와 카운터라는 2가지로 분류할 수 있다.
        - 게이지(Gauge)
            - 임의로 오르내일 수 있는 값
            - 예) CPU 사용량, 메모리 사용량, 사용중인 커넥션
        - 카운터(Counter)
            - 단순하게 증가하는 단일 누적 값
            - 예) HTTP 요청 수, 로그 발생 수

        쉽게 이야기해서 게이지는 오르락 내리락 하는 값이고, 카운터는 특정 이벤트가 발생할 때 마다 그 수를 계속 누적하는 값이다.

        게이지(Gauge)
            게이지는 오르고 내리고 하는 값이다. 게이지는 현재 상태를 그대로 출력하면 된다.
            예를 들어서 대표적인 게이지인 CPU 사용량(system_cpu_usage)을 생각해보자.
            CPU 사용량의 현재 상태를 계속 측정하고 그 값을 그대로 그래프에 출력하면 과거부터 지금까지의 CPU 사용량을 확인할 수 있다.
            게이지는 가장 단순하고 사용하기 쉬운 메트릭이다. 크게 고민하지 않고 있는 그대로를 사용하면 된다.

        카운터(Counter)
            카운터는 단순하게 증가하는 단일 누적 값이다. 예를 들어서 고객의 HTTP 요청수를 떠올려 보면 바로 이해가 될 것이다.
            카운터는 계속 누적해서 증가하는 값이다.
            따라서 계속 증가하는 그래프만 보게 될 것이다.
            이렇게 증가만 하는 그래프에서는 특정 시간에 얼마나 고객의 요청이 들어왔는지 한눈에 확인하기 매우 어렵다.
            이런 문제를 해결하기 위해 increase(), rate() 같은 함수를 지원한다.

            increase({metric name}[{time}])
                increase() 를 사용하면 이런 문제를 해결할 수 있다. 지정한 시간 단위별로 증가를 확인할 수 있다.
                마지막에 [시간] 을 사용해서 범위 벡터를 선택해야 한다.
                예) increase(http_server_requests_seconds_count{uri="/log"}[1m])

            rate({metric name}[{time}])
                범위 백터에서 초당 평균 증가율을 계산한다.
                increase() 가 숫자를 직접 카운트 한다면, rate() 는 여기에 초당 평균을 나누어서 계산한다.
                rate(data[1m]) 에서 [1m] 이라고 하면 60초가 기준이 되므로 60을 나눈 수이다.
                rate(data[2m]) 에서 [2m] 이라고 하면 120초가 기준이 되므로 120을 나눈 수이다.
                예) rate(http_server_requests_seconds_count{uri="/log"}[1m])

            irate({metric name}[{time}])
                rate 와 유사한데 범위 벡터에서 초당 순간 증가율을 계산한다. 급격하게 증가한 내용을 확인하기 좋다.

    참고
        기본기능: https://prometheus.io/docs/prometheus/latest/querying/basics/
        연산자: https://prometheus.io/docs/prometheus/latest/querying/operators/
        함수: https://prometheus.io/docs/prometheus/latest/querying/functions/
    */
}
