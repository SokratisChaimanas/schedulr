SET GLOBAL event_scheduler = ON;

-- Drops the event if it already exists
DROP EVENT IF EXISTS update_event_status1;

-- Creates a new event that runs every 1 minutes
CREATE EVENT update_event_status
ON SCHEDULE EVERY 1 MINUTE
DO
UPDATE schedulrdb.events
SET events.status = 'COMPLETED'
WHERE events.status = 'PENDING' AND events.date <= NOW();
