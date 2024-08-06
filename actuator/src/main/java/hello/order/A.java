package hello.order;

public class A {
    /*
    메트릭 등록
        CPU 사용량, 메모리 사용량, 톰캣 쓰레드, DB 커넥션 풀과 같이 공통으로 사용되는 기술 메트릭은 이미 등록되어 있다.
        우리는 이런 이미 등록된 메트릭을 사용해서 대시보드를 구성하고 모니터링 하면 된다.

        여기서 더 나아가서 비즈니스에 특화된 부분을 모니터링 하고 싶으면 어떻게 해야할까?
        예를 들어서 주문수, 취소수, 재고 수량 같은 메트릭 들이 있다.
        이 부분은 공통으로 만들 수 있는 부분은 아니고, 각각의 비즈니스에 특화된 부분들이다.
        이런 메트릭들도 시스템을 운영하는데 상당히 도움이 된다.
        예를 들어서 취소수가 갑자기 급증하거나 재고 수량이 임계치 이상으로 쌓이는 부분들은 기술적인 메트릭으로 확인할 수 없는 우리 시스템의 비즈니스 문제를 빠르게 파악하는데 도움을 준다.
        예를 들어서 택배회사에 문제가 생겨서 고객들이 많이 기다리다가 지쳐서 취소수가 증가해도 CPU, 메모리 사용량 같은 시스템 메트릭에는 아무런 문제가 발생하지 않는다.
        이럴 때 비즈니스 메트릭이 있으면 이런 문제를 빠르게 인지할 수 있다.

        비즈니스에 관한 부분은 각 비즈니스 마다 구현이 다르다. 따라서 비즈니스 메트릭은 직접 등록하고 확인해야 한다.
        여기서는 우리 비즈니스의 실시간 주문수, 취소수 또 실시간 재고 수량을 메트릭으로 등록하고 확인해보자.
        각각의 메트릭은 다음과 같이 정의했다.

        주문수, 취소수
            - 상품을 주문하면 주문수가 증가한다.
            - 상품을 취소해도 주문수는 유지한다. 대신에 취소수를 증가한다.
        재고 수량
            - 상품을 주문하면 재고 수량이 감소한다.
            - 상품을 취소하면 재고 수량이 증가한다.
            - 재고 물량이 들어오면 재고 수량이 증가한다.
            - 주문수, 취소수는 계속 증가하므로 카운터를 사용하자.
            - 재고 수량은 증가하거나 감소하므로 게이지를 사용하자.

    메트릭 등록1 - 카운터(hello.order.v1 패키지)
        MeterRegistry
            마이크로미터 기능을 제공하는 핵심 컴포넌트
            스프링을 통해서 주입 받아서 사용하고, 이곳을 통해서 카운터, 게이지 등을 등록한다.

        Counter(카운터)
            - https://prometheus.io/docs/concepts/metric_types/#counter
            - 단조롭게 증가하는 단일 누적 측정항목
                - 단일 값
                - 보통 하나씩 증가
                - 누적이므로 전체 값을 포함(total)
                - 프로메테우스에서는 일반적으로 카운터의 이름 마지막에 _total 을 붙여서 my_order_total 과 같이 표현함
            - 값을 증가하거나 0으로 초기화 하는 것만 가능
            - 마이크로미터에서 값을 감소하는 기능도 지원하지만, 목적에 맞지 않음
            - 예) HTTP 요청수

        확인
            1. 주문과 취소를 각각 한번씩 실행
            2. 메트릭 확인(각각 실행해야 메트릭이 등록됨)
                http://localhost/actuator/metrics/my.order
            3. 프로메테우스 확인
                http://localhost/actuator/prometheus

                메트릭 이름이 my.order my_order_total 로 변경된 것을 확인할 수 있다.
                    - 프로메테우스는 . -> _ 로 변경한다.
                    - 카운터는 마지막에 _total 을 붙인다. 프로메테우스는 관례상 카운터 이름의 끝에 _total 을 붙인다.
                    - method 라는 tag, 레이블을 기준으로 데이터가 분류되어 있다.

            4. 그라파나 등록
                - Panel options
                    - Title : 주문수
                - PromQL
                    - increase(my_order_total{method="order"}[1m])
                        - Legend : {{method}}
                    - increase(my_order_total{method="cancel"}[1m])
                        - Legend : {{method}}

    메트릭 등록2 - @Counted(hello.order.v2 패키지)
        앞서 만든 OrderServiceV1 의 가장 큰 단점은 메트릭을 관리하는 로직이 핵심 비즈니스 개발 로직에 침투했다는 점이다.
        이런 부분을 분리하려면 어떻게 해야할까? 바로 스프링 AOP 를 사용하면 된다.
        직접 필요한 AOP 를 만들어서 적용해도 되지만, 마이크로미터는 이런 상황에 맞추어 필요한 AOP 구성요소를 이미 다 만들어두었다.

    메트릭 등록3 - Timer
        Timer
            Timer 는 좀 특별한 메트릭 측정 도구인데, 시간을 측정하는데 사용된다.

            - 카운터와 유사한데, Timer 를 사용하면 실행 시간도 함께 측정할 수 있다.
            - Timer 는 다음과 같은 내용을 한번에 측정해준다.
                - seconds_count : 누적 실행 수 - 카운터
                - seconds_sum : 실행 시간의 합 - sum
                - seconds_max : 최대 실행 시간(가장 오래걸린 실행 시간) - 게이지
            - 내부에 타임 윈도우라는 개념이 있어서 1~3분 마다 최대 실행 시간이 다시 계산된다.

        확인
            1. 주문과 취소를 각각 한번씩 실행
            2. 메트릭 확인(각각 실행해야 메트릭이 등록됨)
                http://localhost/actuator/metrics/my.order
            3. 프로메테우스 확인
                http://localhost/actuator/prometheus

                - seconds_count : 누적 실행 수
                - seconds_sum : 실행 시간의 합
                - seconds_max : 최대 실행 시간(가장 오래 걸린 실행 시간), 프로메테우스 게이지
                    - 참고: 내부에 타임 윈도우라는 개념이 있어서 1~3분 마다 최대 실행 시간이 다시 계산된다.
                - 여기서 평균 실행 시간도 계산할 수 있다.
                    - seconds_sum / seconds_count = 평균 실행시간
                
            4. 그라파나 등록
                주문수
                    - Panel options
                        - Title : 주문수 v3
                    - PromQL
                        - increase(my_order_seconds_count{method="order"}[1m])
                            - Legend : {{method}}
                        - increase(my_order_seconds_count{method="cancel"}[1m])
                            - Legend : {{method}}
                최대 실행시간
                    - Panel options
                        - Title : 최대 실행시간
                    - PromQL
                        - my_order_seconds_max
                            - Legend : {{method}}
                평균 실행시간
                    - Panel options
                        - Title : 평균 실행시간
                    - PromQL
                        - increase(my_order_seconds_sum[1m]) / increase(my_order_seconds_count[1m])
                            - Legend : {{method}}

    메트릭 등록4 - @Timed
        타이머는 @Timed 라는 애노테이션을 통해 AOP 를 적용할 수 있다.

    메트릭 등록5 - 게이지
        Gauge(게이지)
            - https://prometheus.io/docs/concepts/metric_types/#gauge
            - 게이지는 임의로 오르내릴 수 있는 단일 숫자 값을 나타내는 메트릭
            - 값의 현재 상태를 보는데 사용
            - 값이 증가하거나 감소할 수 있음
            - 예) 차량의 속도, CPU 사용량, 메모리 사용량

            참고: 카운터와 게이지를 구분할 때는 값이 감소할 수 있는가를 고민해보면 도움이 된다.

        확인
            1. 메트릭 확인(각각 실행해야 메트릭이 등록됨)
                http://localhost/actuator/metrics/my.stock
            2. 프로메테우스 확인
                http://localhost/actuator/prometheus
            3. 그라파나 등록
                - Panel options
                    - Title : 재고
                - PromQL
                    - my_stock

    참고
        Tag 를 사용하면 데이터를 나누어서 확인할 수 있다.
        Tag 는 카디널리티가 낮으면서 그룹화 할 수 있는 단위에 사용해야 한다.
        예) 성별, 주문 상태, 결제 수단[신용카드, 현금] 등등
        카디널리티가 높으면 안된다. 예) 주문번호, PK 같은 것
    */
}
