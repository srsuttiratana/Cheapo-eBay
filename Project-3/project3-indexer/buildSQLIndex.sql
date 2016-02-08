CREATE TABLE IF NOT EXISTS ItemLocation
(ItemID INT NOT NULL, 
Location GEOMETRY NOT NULL, 
PRIMARY KEY (ItemID)) ENGINE=MyISAM;

INSERT INTO ItemLocation (ItemID,Location) 
SELECT ItemID,POINT(Latitude,Longitude) 
FROM Item 
WHERE Latitude IS NOT NULL AND Longitude IS NOT NULL;

CREATE SPATIAL INDEX sp_index ON ItemLocation (Location);