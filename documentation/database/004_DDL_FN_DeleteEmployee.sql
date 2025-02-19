/* === ROLLBACK ===
* drop function fn_delete_employee;
*/

-- create function fn_delete_employee
CREATE OR REPLACE FUNCTION fn_delete_employee(id INT)
RETURNS BOOLEAN
LANGUAGE plpgsql
AS $$
DECLARE
    record_count INT;
BEGIN
    DELETE FROM cat_employee WHERE emp_id = id;
    
    GET DIAGNOSTICS record_count = ROW_COUNT;
    
    IF record_count > 0 THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END;
$$;