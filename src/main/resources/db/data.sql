-- 유저 더미 데이터
INSERT INTO user_tb (username, password, created_at) VALUES ('ssar', '$2a$10$v2smN3fzz4YAwUyxTtcBN.iMIsgi0BZUUMgnqnSvndLp2LheBprVm', NOW());
INSERT INTO user_tb (username, password, created_at) VALUES ('cos', '$2a$10$v2smN3fzz4YAwUyxTtcBN.iMIsgi0BZUUMgnqnSvndLp2LheBprVm', NOW());

-- 게시글 더미 데이터
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('첫 번째 게시글', '안녕하세요. ssar의 첫 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('두 번째 게시글', '안녕하세요. ssar의 두 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('세 번째 게시글', '안녕하세요. cos의 첫 번째 글입니다.', 2, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('네 번째 게시글', '내용 4', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('다섯 번째 게시글', '내용 5', 2, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('여섯 번째 게시글', '내용 6', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('일곱 번째 게시글', '내용 7', 2, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('여덟 번째 게시글', '내용 8', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('아홉 번째 게시글', '내용 9', 2, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열 번째 게시글', '내용 10', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열한 번째 게시글', '내용 11', 2, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열두 번째 게시글', '내용 12', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열세 번째 게시글', '내용 13', 2, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열네 번째 게시글', '내용 14', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열다섯 번째 게시글', '내용 15', 2, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열여섯 번째 게시글', '내용 16', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열일곱 번째 게시글', '내용 17', 2, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열여덟 번째 게시글', '내용 18', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('열아홉 번째 게시글', '내용 19', 2, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('스무 번째 게시글', '내용 20', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('스물한 번째 게시글', '내용 21', 2, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('스물두 번째 게시글', '내용 22', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('스물세 번째 게시글', '내용 23', 2, NOW());

-- 댓글 더미 데이터
INSERT INTO reply_tb (comment, user_id, board_id, created_at) VALUES ('첫 번째 게시글에 ssar이 작성한 댓글입니다.', 1, 1, NOW());
INSERT INTO reply_tb (comment, user_id, board_id, created_at) VALUES ('첫 번째 게시글에 cos가 작성한 댓글입니다.', 2, 1, NOW());
INSERT INTO reply_tb (comment, user_id, board_id, created_at) VALUES ('두 번째 게시글에 cos가 작성한 댓글입니다.', 2, 2, NOW());
