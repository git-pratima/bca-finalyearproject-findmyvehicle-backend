DO $$
BEGIN
    IF to_regclass('tbl_vehicle_images') IS NOT NULL THEN
        ALTER TABLE tbl_vehicle_images ALTER COLUMN vehicle_id DROP NOT NULL;
    END IF;
END
$$;
