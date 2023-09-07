-- Drop existing tables if they exist
DROP TABLE IF EXISTS Terminated_Employee CASCADE;
DROP TABLE IF EXISTS Employee CASCADE;
DROP TABLE IF EXISTS Role CASCADE;

-- Drop existing triggers and functions if they exist
DROP FUNCTION IF EXISTS calculate_bonus() CASCADE;
DROP FUNCTION IF EXISTS recalculate_employee_bonus() CASCADE;
DROP FUNCTION IF EXISTS move_terminated_employee() CASCADE;
DROP FUNCTION IF EXISTS assign_next_employee_id() CASCADE;

-- Create Role Table
CREATE TABLE Role (
    role_id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE,
    bonus FLOAT
);

-- Populate Role Table
INSERT INTO Role (name, bonus) VALUES ('Manager', 10), ('Supervisor', 6), ('Staff', 3)
ON CONFLICT (name) DO NOTHING;

-- Create Employee Table
CREATE TABLE Employee (
    employee_id BIGINT PRIMARY KEY,
    name VARCHAR(50),
    salary BIGINT,
    grade INT REFERENCES Role(role_id),
    total_bonus BIGINT
);

-- Create Terminated_Employee Table
CREATE TABLE Terminated_Employee (
    employee_id BIGINT PRIMARY KEY,
    name VARCHAR(50),
    salary BIGINT,
    grade INT,
    total_bonus BIGINT,
    termination_date TIMESTAMP DEFAULT current_timestamp
);

-- Function to Assign employee_id by incrementing 1 to the maximum value of employee_id, 1 if no employees curernly exist
CREATE OR REPLACE FUNCTION assign_next_employee_id()
RETURNS TRIGGER AS $$
BEGIN
    -- Find the maximum employee_id from both Employee and Terminated_Employee tables
    SELECT COALESCE(MAX(employee_id), 0) + 1 INTO NEW.employee_id
    FROM (
        SELECT employee_id FROM Employee
        UNION ALL
        SELECT employee_id FROM terminated_employee
    ) AS combined_employee_ids;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to Assign Next Available Employee ID on INSERT
CREATE TRIGGER assign_next_employee_id_trigger
BEFORE INSERT ON Employee
FOR EACH ROW EXECUTE FUNCTION assign_next_employee_id();

-- Function to Calculate Bonus in Employee Table for newly inserted or updated Employee
CREATE OR REPLACE FUNCTION calculate_bonus()
RETURNS TRIGGER AS $$
BEGIN
    NEW.total_bonus := NEW.salary * (1 + (SELECT bonus FROM Role WHERE role_id = NEW.grade) / 100);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to Auto Calculate Bonus in Employee Table ON INSERT or UPDATE in Employee
CREATE TRIGGER auto_calculate_bonus
BEFORE INSERT OR UPDATE ON Employee
FOR EACH ROW EXECUTE FUNCTION calculate_bonus();

-- Function to Recalculate Employee Bonus for all Employees with corresponding role_id/grade
CREATE OR REPLACE FUNCTION recalculate_employee_bonus()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE Employee 
    SET total_bonus = salary * (1 + NEW.bonus / 100)
    WHERE grade = NEW.role_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to Recalculate Bonus on Role Change
CREATE TRIGGER recalculate_bonus_on_role_change
AFTER INSERT OR UPDATE ON Role
FOR EACH ROW EXECUTE FUNCTION recalculate_employee_bonus();

-- Function to Move Terminated Employees to Terminated_Employee Table for the deleted Employee
CREATE OR REPLACE FUNCTION move_terminated_employee()
RETURNS TRIGGER AS $$
BEGIN
    -- Check if the function is already running to avoid recursion
    IF current_setting('custom.is_running', TRUE) = 'true' THEN
        RETURN NEW;
    END IF;

    -- Set a custom configuration parameter to indicate the function is running
    PERFORM set_config('custom.is_running', 'true', FALSE);

    -- Perform the actions
    INSERT INTO terminated_employee (employee_id, name, salary, grade, total_bonus) 
    VALUES (OLD.employee_id, OLD.name, OLD.salary, OLD.grade, OLD.total_bonus);

    -- Reset the custom configuration parameter
    PERFORM set_config('custom.is_running', 'false', FALSE);

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

-- Trigger to Move Terminated Employees on DELETE of an Employee 
CREATE TRIGGER handle_terminated_employee
BEFORE DELETE ON Employee
FOR EACH ROW EXECUTE FUNCTION move_terminated_employee();
