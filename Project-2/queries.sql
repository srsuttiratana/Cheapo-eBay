SELECT COUNT(*) AS 'Number of Users' FROM User;

SELECT COUNT(*) AS 'Number of Items From New York' FROM Item WHERE BINARY Location = 'New York';

--3 is incorrect--
SELECT COUNT(*) FROM Category GROUP BY ItemID, Category HAVING COUNT(*) = 4;

--Still need 4--

SELECT COUNT(*) FROM User WHERE Seller_Rating > 1000;

SELECT COUNT(DISTINCT UserID) FROM User, Item, Bid WHERE (UserID = SellerID AND SellerID = BidderID AND UserID = BidderID);

--7 is incorrect--
SELECT COUNT(*) FROM Bid, Category WHERE Amount > 100;