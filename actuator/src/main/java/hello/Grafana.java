package hello;

public class Grafana {
    /*
    그라파나
        프로메테우스가 DB 라고 하면 이 DB에 있는 데이터를 불러서 사용자가 보기 편하게 보여주는 대시보드가 필요하다.
        그라파나는 매우 유연하고 데이터를 그래프로 보여주는 툴이다.
        수 많은 그래프를 제공하고 프로메테우스를 포함한 다양한 데이터소스를 지원한다.

    그라파나 설치
        https://grafana.com/grafana/download

    그라파나 실행
        1. grafana-server.exe 실행 후 http://localhost:3000
        2. 로그인 화면
            email: admin
            password: admin
        3. 비밀번호를 설정하라고 하면 skip

    그라파나 - 프로메테우스 연동
        1. 왼쪽 메뉴에 Connections 선택
        2. Add new Connection 선택
        3. 프로메테우스 선택 후 URL 만 http://localhost:9090
            나머지는 특별히 고칠 부분이 없다면 그대로 두고 Save & Test

    그라파나 - 대시보드 저장
        1. 왼쪽 메뉴에 대시보드 선택
        2. New 버튼 -> New Dashboard 선택
        3. 오른쪽 상단에 Save dashboard(disk 아이콘) 클릭
        4. 대시보드 이름 설정 후 저장

    그라파나 - 대시보드 확인
        1. 왼쪽 메뉴에 대시보드 선택
        2. 앞서 만든 대시보드 선택

    그라파나 - 대시보드 패널 만들기
        1. Add visualization
        2. 사용할 데이터 소스 선택(프로메테우스)
        3. 아래에 보면 Run queries 버튼 오른쪽에 Builder, Code 라는 탭이 보이는데 Code 선택
        4. Enter a PromQL query... 이라는 부분에 메트릭을 입력

    예제
        CPU 패널 만들기
            1. PromQL 에 system_cpu_usage 를 입력하고 Run queries 버튼 클릭
            2. 하단에 Add query 클릭
            3. PromQL 에 process_cpu_usage 를 입력하고 Run queries 버튼 클릭
            4. 쿼리 별로 Options 를 선택하고 Legend 를 Custom 으로 변경
            5. 각각 차트에 표시할 이름으로 변경
            6. 오른쪽에 Panel options 라는 부분에 Title 을 변경
            7. 오른쪽 상단에 Save 또는 Apply 선택

        디스크 사용량 패널 만들기
            1. PromQL 에 disk_total_bytes 를 입력하고 Run queries 버튼 클릭
            2. 하단에 Add query 클릭
            3. PromQL 에 disk_total_bytes - disk_free_byte 를 입력하고 Run queries 버튼 클릭(사용 디스크 용량 = 전체 디스크 용량 - 남은 디스크 용량)
            4. 쿼리 별로 Options 를 선택하고 Legend 를 Custom 으로 변경
            5. 각각 차트에 표시할 이름으로 변경
            6. 오른쪽에 Panel options 라는 부분에 Title 을 변경
            7. Panel options 에 Standard options 에 Unit 란 Data -> bytes(SI) 선택
            8. Panel options 에 Standard options 에 Min 란 0 입력
            9. 오른쪽 상단에 Save 또는 Apply 선택

    그라파나 - 공유 대시보드
        https://grafana.com/grafana/dashboards/

        스프링 부트 시스템 모니터 대시보드 불러오기
            1. https://grafana.com/grafana/dashboards/11378-justai-system-monitor/ 접속
            2. Copy Id to clipboard 를 선택 또는 ID: **** 이라고 되어 있는 부분의 숫자를 저장
            3. 그라파나로 돌아와서 왼쪽 메뉴에 대시보드 선택
            4. New 버튼 -> Import 선택
            5. 불러올 대시보드 숫자를 입력하고 Load 버튼 선택
            6. 프로메테우스 데이터소스를 선택하고 Import 버튼 선택

        대시보드를 수정하려면 먼저 수정모드로 변경해야 한다.
            오른쪽 상단의 설정 버튼(톱니바퀴, Dashboard settings)을 선택 Make editable 선택
    */
}
