-- 清理旧表
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS reviews;

-- 创建新表
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       handle VARCHAR(255) UNIQUE NOT NULL -- 用户的唯一标识，如 @WilsonChen
);

CREATE TABLE tweets (
                        id SERIAL PRIMARY KEY,
                        user_id INT NOT NULL,
                        content VARCHAR(280) NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                        FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE follows (
                         follower_id INT NOT NULL, -- 粉丝的用户ID
                         following_id INT NOT NULL, -- 被关注者的用户ID
                         PRIMARY KEY (follower_id, following_id),
                         FOREIGN KEY (follower_id) REFERENCES users(id),
                         FOREIGN KEY (following_id) REFERENCES users(id)
);

-- 插入种子数据
INSERT INTO users (username, handle) VALUES
                                         ('Wilson Chen', 'wilson'),
                                         ('Tony Stark', 'ironman'),
                                         ('Steve Rogers', 'captain');

INSERT INTO tweets (user_id, content) VALUES
                                          (1, '正在学习 R2DBC!'),
                                          (2, 'I am Iron Man.'),
                                          (3, 'I can do this all day.'),
                                          (2, 'Love you 3000.');

-- Wilson 关注了 Tony 和 Steve
INSERT INTO follows (follower_id, following_id) VALUES (1, 2), (1, 3);