

CREATE TABLE projects (
    id VARCHAR(50) NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE inboxes (
    id MEDIUMINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    projectId VARCHAR(50),
    tenantId int default 0,
    CONSTRAINT UC_inbox UNIQUE (projectId,tenantId),
    CONSTRAINT fk_projects_inbox FOREIGN KEY (projectId) REFERENCES projects(id)
);

CREATE TABLE tasks (
    id VARCHAR(50) NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    status VARCHAR(50),
    completion_date DATE,
    projectId VARCHAR(50),

    CONSTRAINT fk_projects FOREIGN KEY (projectId) REFERENCES projects(id)
);
