ALTER TABLE job_application
  DROP CONSTRAINT IF EXISTS job_application_status_check;

ALTER TABLE job_application
  ALTER COLUMN status TYPE varchar(255)
  USING CASE status
    WHEN 0 THEN 'SENT'
    WHEN 1 THEN 'IN_REVIEW'
    WHEN 2 THEN 'ACCEPTED'
    WHEN 3 THEN 'REJECTED'
    ELSE NULL
  END;
