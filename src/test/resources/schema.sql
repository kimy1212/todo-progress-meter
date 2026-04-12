CREATE TABLE IF NOT EXISTS todo_tabs (
    todo_tab_id BIGSERIAL NOT NULL,
    todo_tab_name VARCHAR(255) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (todo_tab_id),
    UNIQUE (user_id, todo_tab_name)
);

CREATE TABLE IF NOT EXISTS todos (
    todo_id BIGSERIAL NOT NULL,
    todo_name VARCHAR(255) NOT NULL,
    todo_tab_id BIGINT NOT NULL,
    progress_total INT NOT NULL DEFAULT 100,
    progress_completed INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (todo_id),
    UNIQUE (todo_tab_id, todo_name),
    CONSTRAINT fk_todo_tab FOREIGN KEY (todo_tab_id)
        REFERENCES todo_tabs(todo_tab_id)
        ON DELETE CASCADE,
    CHECK (progress_total > 0),
    CHECK (progress_completed BETWEEN 0 AND progress_total)
);

CREATE INDEX IF NOT EXISTS idx_todos_todo_tab_id ON todos (todo_tab_id);