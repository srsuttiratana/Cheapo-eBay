SELECT COUNT(*) AS 'Number of Users' FROM User;

SELECT COUNT(*) AS 'Number of Items From New York' FROM Item WHERE BINARY Location = 'New York';

SELECT COUNT(ItemID) FROM (SELECT ItemID FROM Item_Category GROUP BY ItemID HAVING COUNT(Category) = 4)S;

--Still need 4--

SELECT COUNT(*) FROM User WHERE Seller_Rating > 1000;

SELECT COUNT(DISTINCT UserID) FROM User, Item, Bid WHERE (UserID = SellerID AND SellerID = BidderID AND UserID = BidderID);

SELECT COUNT(DISTINCT Category) FROM Item_Category
WHERE ItemID IN (SELECT ItemID FROM Bid WHERE Amount > 100.00);