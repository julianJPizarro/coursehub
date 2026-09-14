ALTER TABLE enrollment
    ADD CONSTRAINT ck_completed_has_grade
        CHECK (status <> 'COMPLETED' OR final_grade IS NOT NULL);
