CREATE TABLE api_route
(
    id SERIAL PRIMARY KEY,
    path character varying COLLATE pg_catalog."default" NOT NULL,
    method character varying(10) COLLATE pg_catalog."default",
    route character varying COLLATE pg_catalog."default" NOT NULL,
    privilege character varying COLLATE pg_catalog."default"
)
TABLESPACE pg_default;
ALTER TABLE api_route OWNER to postgres;
INSERT INTO api_route(method, path, route, privilege) VALUES ('', '/home', 'http://home.local:3000', 'P_HOME,P_HOME_DETAIL');
INSERT INTO api_route(method, path, route, privilege) VALUES ('GET,PUT', '/purchase/**', 'http://account.local:3000', 'P_USER');
INSERT INTO api_route(method, path, route, privilege) VALUES ('POST', '/purchase/**', 'http://account.local:3000', 'P_HOME,P_PURCHASE');