-- 세션 서버 읽기 API 테스트용 Lua 스크립트
-- 사용법: docker-compose run --rm wrk -t12 -c400 -d30s --script=read-session.lua http://localhost:8080/api/read

-- 랜덤 세션 ID 생성
math.randomseed(os.time())

-- HTTP 요청 생성 함수
function request()
    -- 랜덤 세션 ID 생성
    local sessionId = "session_" .. math.random(10000, 99999)
    
    -- URL 파라미터로 세션 ID 전달
    local path = "/api/read?sessionId=" .. sessionId
    
    -- HTTP 헤더 설정
    local headers = {}
    headers["Accept"] = "application/json"
    
    -- GET 요청 반환
    return wrk.format("GET", path, headers, nil)
end

-- 응답 처리 함수
function response(status, headers, body)
    if status ~= 200 then
        print("Error: " .. status .. " - " .. body)
    end
end
