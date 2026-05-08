alter table if exists job_application
   add constraint uc_engineer_posting unique (posting_id, engineer_id);
