
USE `running-platform`;
ALTER TABLE users
MODIFY location_detail POINT SRID 4326 NOT NULL;
ALTER TABLE users
ADD SPATIAL INDEX idx_users_location_detail (location_detail);
SHOW INDEX FROM users;

ALTER TABLE users
DROP INDEX idx_users_location_detail;
 
EXPLAIN ANALYZE
SELECT 
    u.id AS id,
    u.username AS username,
    u.full_name AS fullName,
    u.image_url AS imageUrl,
    u.location AS location,
    ROUND(ST_Distance_Sphere(u.location_detail, cu.location_detail) / 1000, 1) AS distanceKm  
FROM users u
JOIN users cu ON cu.id = 1
WHERE u.id <> 1
  AND u.email_verified = true
  AND NOT EXISTS (
      SELECT 1
      FROM friend_ships f1
      WHERE f1.requester_id = 1
        AND f1.addressee_id = u.id
        AND f1.status = 'ACCEPTED'
  )
  AND NOT EXISTS (
      SELECT 1
      FROM friend_ships f2
      WHERE f2.requester_id = u.id
        AND f2.addressee_id = 1
        AND f2.status = 'ACCEPTED'
  )
  AND (
      NULL IS NULL OR NULL = ''
      OR LOWER(u.username) LIKE LOWER(CONCAT('%', NULL, '%'))
      OR LOWER(u.full_name) LIKE LOWER(CONCAT('%', NULL, '%'))
      OR LOWER(u.phone_number) LIKE LOWER(CONCAT('%', NULL, '%'))
  )
  AND (
      5 IS NULL
      OR ST_Distance_Sphere(u.location_detail, cu.location_detail) / 1000 <= 5
  )
ORDER BY u.created_at DESC;