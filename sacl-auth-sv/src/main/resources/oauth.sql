CREATE TABLE OAUTH_CLIENT_DETAILS (
    CLIENT_ID VARCHAR(250) PRIMARY KEY,
    RESOURCE_IDS VARCHAR(256),
    CLIENT_SECRET VARCHAR(256),
    SCOPE VARCHAR(256),
    AUTHORIZED_GRANT_TYPES VARCHAR(256),
    WEB_SERVER_REDIRECT_URI VARCHAR(256),
    AUTHORITIES VARCHAR(256),
    ACCESS_TOKEN_VALIDITY INTEGER,
    REFRESH_TOKEN_VALIDITY INTEGER,
    ADDITIONAL_INFORMATION VARCHAR(4096),
    AUTOAPPROVE VARCHAR(256)
);

INSERT INTO OAUTH_CLIENT_DETAILS
    (client_id, client_secret, scope, authorized_grant_types,
    web_server_redirect_uri, authorities, access_token_validity,
    refresh_token_validity, additional_information, autoapprove)
VALUES
    ('sacl-dubbo-prov-sv', '$2a$10$VBow9zBRuB8SRy.ByuALzOPQmbkNQ1I.tPTs1kV8Je1miO26qEk1K', 'all',
    'authorization_code,refresh_token,password', null, null, 3600, 36000, null, true);

INSERT INTO OAUTH_CLIENT_DETAILS
    (client_id, client_secret, scope, authorized_grant_types,
    web_server_redirect_uri, authorities, access_token_validity,
    refresh_token_validity, additional_information, autoapprove)
VALUES
    ('sacl-online-sv', '$2a$10$GIBEQQc1teCUR245/ZrR8OAsBg3FtOUw7dRguWorf3OBw6ZnrSImq', 'all',
    'authorization_code,refresh_token,password', null, null, 3600, 36000, null, true);