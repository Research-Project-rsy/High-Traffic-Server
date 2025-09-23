-- 세션 서버 쓰기 API 테스트용 Lua 스크립트
-- 사용법: docker-compose run --rm wrk -t12 -c400 -d30s --script=post-session.lua http://localhost:8080/api/write

-- 랜덤 세션 ID 생성
math.randomseed(os.time())

-- HTTP 요청 생성 함수
function request()
    -- 랜덤 세션 ID 생성 (예: session_12345)
    local sessionId = "session_" .. math.random(10000, 99999)
    
    -- 랜덤 사용자 ID 생성
    local userId = "user_" .. math.random(1000, 9999)
    
    -- 현재 시간을 밀리초로 생성
    local timestamp = os.time() * 1000 + math.random(0, 999)
    
    -- JSON 페이로드 생성
    local body = string.format([[
        {
            "sessionId": "%s",
            "userId": "%s",
            "timestamp": %d,
            "data": {
                "action": "test_action",
                "value": %d,
                "message": "Performance test data"
            }
        }
    ]], sessionId, userId, timestamp, math.random(1, 100))
    
    -- HTTP 헤더 설정
    local headers = {}
    headers["Content-Type"] = "application/json"
    headers["Accept"] = "application/json"
    
    -- POST 요청 반환
    return wrk.format("POST", nil, headers, body)
end

-- 응답 처리 함수 (선택사항)
function response(status, headers, body)
    -- 200 OK가 아닌 경우 로깅
    if status ~= 200 then
        print("Error: " .. status .. " - " .. body)
    end
end

-- 테스트 완료 후 통계 출력 (선택사항)
function done(summary, latency, requests)
    print("\n=== 테스트 완료 ===")
    print("총 요청 수: " .. summary.requests)
    print("총 시간: " .. summary.duration / 1000000 .. "초")
    print("평균 응답 시간: " .. latency.mean / 1000 .. "ms")
    print("최대 응답 시간: " .. latency.max / 1000 .. "ms")
    print("초당 요청 수: " .. summary.requests / (summary.duration / 1000000))
end
