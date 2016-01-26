CREATE TABLE User (
UserID VARCHAR(100) NOT NULL,
Location VARCHAR(50) DEFAULT NULL,
Country VARCHAR(30) DEFAULT NULL,
Seller_Rating INTEGER,
Bidder_Rating INTEGER,
PRIMARY KEY(UserID)
)ENGINE = MYISAM DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE Item (
ItemID INTEGER NOT NULL,
Name VARCHAR(100),
Currently DECIMAL(8,2) NOT NULL,
Buy_Price DECIMAL (8,2) DEFAULT NULL,
First_Bid DECIMAL (8,2) NOT NULL,
Number_of_Bids INTEGER,
Location VARCHAR(50) DEFAULT NULL,
Longitude VARCHAR(50) DEFAULT NULL,
Latitude VARCHAR(50) DEFAULT NULL,
Country VARCHAR(30) DEFAULT NULL,
Started TIMESTAMP NOT NULL,
Ends TIMESTAMP NOT NULL,
SellerID VARCHAR(100),
Description VARCHAR(4000),
PRIMARY KEY(ItemID),
FOREIGN KEY(SellerID) REFERENCES User(UserID)
)ENGINE = MYISAM DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE Bid (
BidderID VARCHAR(100) NOT NULL,
Time TIMESTAMP NOT NULL,
Amount DECIMAL(8,2),
ItemID INTEGER NOT NULL,
PRIMARY KEY(BidderID, Time, ItemID),
FOREIGN KEY (ItemID) REFERENCES Item (ItemID),
FOREIGN KEY (BidderID) REFERENCES User (UserID)
)ENGINE = MYISAM DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE Category (
ItemID INTEGER NOT NULL,
Category VARCHAR(100) NOT NULL,
PRIMARY KEY(ItemID, Category),
FOREIGN KEY(ItemID) REFERENCES Item(ItemID)
)ENGINE = MYISAM DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;