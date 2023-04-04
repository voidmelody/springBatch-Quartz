# springBatch-Quartz
Spring Batch와 Quartz scheduelr를 활용한 구현 예제입니다.

<23.4.4>
오류 해결 부분)
JobDataMap에 executeCount 변수를 활용해서 실행 횟수를 지정해서 job이 실행하길 원했으나,
job이 끝날 때마다 해당 값이 초기화가 되어 있기 때문에 지정이 불가한 오류를 찾음.
또 엄청난 삽질을 한 결과, 해당 Job이 끝나도 해당 데이터를 유지하기 위한 @PersistJobDataAfterExecution을 활용했음.
구글링해서 볼 수 있는 예제들 중 상당수가 해당 내용을 언급 안해서 참고하길 바람.

