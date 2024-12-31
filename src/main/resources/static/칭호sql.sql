-- 최초 운동 기록 테이블 생성
CREATE TABLE user_first_workout (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    workout_type_id BIGINT NOT NULL,
    achieved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_workout_type (user_id, workout_type_id),
    CONSTRAINT fk_first_workout_user FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_first_workout_type FOREIGN KEY (workout_type_id) REFERENCES workout_type (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 칭호 달성 조건 추가
INSERT INTO item_condition 
(achievement_type, description, required_count, workout_type_id) 
VALUES 
-- 기본 달성 조건
('ACHIEVEMENT', '첫 운동일지 기록', 1, NULL),
('ACHIEVEMENT', '어제 운동을 갔지만 오늘 가지않고 다음날이 되었음', 1, NULL),
('ACHIEVEMENT', '연속으로 3일 운동 기록', 3, NULL),
('ACHIEVEMENT', '연속으로 30일 운동 기록', 30, NULL),
-- 각 부위별 첫 운동 조건
('ACHIEVEMENT', '처음으로 등 운동 기록', 1, 1),
('ACHIEVEMENT', '처음으로 가슴 운동 기록', 1, 2),
('ACHIEVEMENT', '처음으로 어깨 운동 기록', 1, 7),
('ACHIEVEMENT', '처음으로 삼두 운동 기록', 1, 5),
('ACHIEVEMENT', '처음으로 이두 운동 기록', 1, 4),
('ACHIEVEMENT', '처음으로 코어 운동 기록', 1, 6),
('ACHIEVEMENT', '처음으로 유산소 운동 기록', 1, 8),
-- 장기 연속 달성 조건
('ACHIEVEMENT', '연속으로 100일 운동 기록', 100, NULL),
('ACHIEVEMENT', '연속으로 365일 운동 기록', 365, NULL),
('ACHIEVEMENT', '연속으로 500일 운동 기록', 500, NULL);

-- 칭호 아이템 추가
INSERT INTO shop_item 
(type_id, condition_id, name, description, price, is_limited)
SELECT 
    (SELECT id FROM item_type WHERE type = 'TITLE'),
    ic.id,
    CASE 
        -- 기본 달성 칭호
        WHEN ic.description = '첫 운동일지 기록' THEN '원래 시작이 제일 어려워'
        WHEN ic.description = '어제 운동을 갔지만 오늘 가지않고 다음날이 되었음' THEN '너 여기서 뭐해'
        WHEN ic.description = '연속으로 3일 운동 기록' THEN '작심삼일 극복'
        WHEN ic.description = '연속으로 30일 운동 기록' THEN '꾸준함이 제일 중요해'
        -- 부위별 첫 운동 칭호
        WHEN ic.description = '처음으로 등 운동 기록' THEN '남자는 등으로 얘기한다'
        WHEN ic.description = '처음으로 가슴 운동 기록' THEN '남자는 가슴이지'
        WHEN ic.description = '처음으로 어깨 운동 기록' THEN '미래의 어깨깡패'
        WHEN ic.description = '처음으로 삼두 운동 기록' THEN '미래의 마동석'
        WHEN ic.description = '처음으로 이두 운동 기록' THEN '미래의 마동석2'
        WHEN ic.description = '처음으로 코어 운동 기록' THEN '빨래판 필요하니?'
        WHEN ic.description = '처음으로 유산소 운동 기록' THEN '백만불짜리 다리'
        -- 장기 연속 달성 칭호
        WHEN ic.description = '연속으로 100일 운동 기록' THEN '헬창꿈나무'
        WHEN ic.description = '연속으로 365일 운동 기록' THEN '너 트레이너지?'
        WHEN ic.description = '연속으로 500일 운동 기록' THEN '너 헬스장차렸니?'
    END as name,
    ic.description,
    0,  -- 모든 칭호는 조건 달성으로만 획득 가능하도록 가격을 0으로 설정
    FALSE  -- 한정판 여부
FROM item_condition ic
WHERE ic.description IN (
    '첫 운동일지 기록',
    '어제 운동을 갔지만 오늘 가지않고 다음날이 되었음',
    '연속으로 3일 운동 기록',
    '연속으로 30일 운동 기록',
    '처음으로 등 운동 기록',
    '처음으로 가슴 운동 기록',
    '처음으로 어깨 운동 기록',
    '처음으로 삼두 운동 기록',
    '처음으로 이두 운동 기록',
    '처음으로 코어 운동 기록',
    '처음으로 유산소 운동 기록',
    '연속으로 100일 운동 기록',
    '연속으로 365일 운동 기록',
    '연속으로 500일 운동 기록'
);