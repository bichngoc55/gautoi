DO $$
DECLARE
db_name text := 'gautoidb';
    user_name text := 'gautoi';
    user_password text := 'gautoi';
    db_exists boolean;
    user_exists boolean;
BEGIN
    -- Check if the database exists
SELECT EXISTS(SELECT 1 FROM pg_database WHERE datname = db_name) INTO db_exists;
IF NOT db_exists THEN
        EXECUTE format('CREATE DATABASE %I', db_name);
END IF;

    -- Check if the user exists
SELECT EXISTS(SELECT 1 FROM pg_roles WHERE rolname = user_name) INTO user_exists;
IF NOT user_exists THEN
        EXECUTE format('CREATE USER %I WITH PASSWORD %L', user_name, user_password);
END IF;

    -- Grant privileges
EXECUTE format('GRANT ALL PRIVILEGES ON DATABASE %I TO %I', db_name, user_name);

-- Make user superuser
EXECUTE format('ALTER USER %I WITH SUPERUSER', user_name);
END
$$;
