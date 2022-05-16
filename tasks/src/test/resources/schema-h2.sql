CREATE TABLE IF NOT EXISTS projects (
    id VARCHAR(50) NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS inboxes (
    id MEDIUMINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    project_id VARCHAR(50),
    tenantId int default 0,
    CONSTRAINT UC_inbox UNIQUE (project_id,tenantId),
    CONSTRAINT fk_projects_inbox FOREIGN KEY (project_id) REFERENCES projects(id)
);

CREATE TABLE IF NOT EXISTS tasks (
    id VARCHAR(50) NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    status VARCHAR(50),
    completion_date TIMESTAMP NULL DEFAULT NULL,
    project_id VARCHAR(50),
    due_date DATE,
    CONSTRAINT fk_projects FOREIGN KEY (project_id) REFERENCES projects(id)
);

CREATE TABLE IF NOT EXISTS tasks (
    id VARCHAR(50) NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    status VARCHAR(50),
    completion_date TIMESTAMP NULL DEFAULT NULL,
    project_id VARCHAR(50),

    CONSTRAINT fk_projects FOREIGN KEY (project_id) REFERENCES projects(id)
);