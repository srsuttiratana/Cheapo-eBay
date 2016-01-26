LOAD DATA LOCAL INFILE './User.dat' INTO TABLE User
FIELDS TERMINATED BY ' |*| '
LINES TERMINATED BY "\n"
(UserID, @location, @country, Seller_Rating, Bidder_Rating) SET Location = nullif(@location, ''),
Country = nullif(@country, '')
;

LOAD DATA LOCAL INFILE './Item.dat' INTO TABLE Item
FIELDS TERMINATED BY ' |*| '
LINES TERMINATED BY "\n"
(ItemID, Name, Currently, @buy_price, First_Bid, @number_of_bids, @location, @longitude, @latitude, @country, @started, @ends, SellerID, Description) SET Buy_Price = nullif(@buy_price, ''),
Location = nullif(@location, ''),
Longitude = nullif(@longitude, ''),
Latitude = nullif(@latitude, ''),
Country = nullif(@country, ''),
Started = STR_TO_DATE(@started, "%Y-%m-%d %H:%i:%s"),
Ends = STR_TO_DATE(@ends, "%Y-%m-%d %H:%i:%s")
;

LOAD DATA LOCAL INFILE './Bid.dat' INTO TABLE Bid
FIELDS TERMINATED BY ' |*| '
LINES TERMINATED BY "\n"
(BidderID, @time, Amount, ItemID) SET Time = STR_TO_DATE(@time, "%Y-%m-%d %H:%i:%s")
;

LOAD DATA LOCAL INFILE './Category.dat' INTO TABLE Category
FIELDS TERMINATED BY ' |*| '
LINES TERMINATED BY "\n"
;