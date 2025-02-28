/* === ROLLBACK ===
* SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = 'jlmorab' AND pid <> pg_backend_pid();
* DROP DATABASE IF EXISTS jlmorab;
*/

-- create database jlmorab
CREATE DATABASE jlmorab;