-- 유저 더미 데이터
INSERT INTO user_tb (username, password, created_at) VALUES ('ssar', '$2a$10$v2smN3fzz4YAwUyxTtcBN.iMIsgi0BZUUMgnqnSvndLp2LheBprVm', NOW());
INSERT INTO user_tb (username, password, created_at) VALUES ('cos', '$2a$10$v2smN3fzz4YAwUyxTtcBN.iMIsgi0BZUUMgnqnSvndLp2LheBprVm', NOW());

-- 게시글 더미 데이터
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('첫 번째 게시글', '안녕하세요. ssar의 첫 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('두 번째 게시글', '안녕하세요. ssar의 두 번째 글입니다.', 1, NOW());
INSERT INTO board_tb (title, content, user_id, created_at) VALUES ('세 번째 게시글', '안녕하세요. cos의 첫 번째 글입니다.', 2, NOW());

-- 댓글 더미 데이터
INSERT INTO reply_tb (comment, user_id, board_id, created_at) VALUES ('첫 번째 게시글에 ssar이 작성한 댓글입니다.', 1, 1, NOW());
INSERT INTO reply_tb (comment, user_id, board_id, created_at) VALUES ('첫 번째 게시글에 cos가 작성한 댓글입니다.', 2, 1, NOW());
INSERT INTO reply_tb (comment, user_id, board_id, created_at) VALUES ('두 번째 게시글에 cos가 작성한 댓글입니다.', 2, 2, NOW());
