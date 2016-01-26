SELECT COUNT(*) AS 'Number of Users' FROM User;

SELECT COUNT(*) AS 'Number of Items From New York' FROM Item WHERE BINARY Location = 'New York';

SELECT COUNT(ItemID) AS 'Number of Auctions Belonging to Exactly Four Categories' FROM (SELECT ItemID FROM Item_Category GROUP BY ItemID HAVING COUNT(Category) = 4)S;

SELECT Bid.ItemID FROM Bid INNER JOIN Item ON Bid.ItemID = Item.ItemID WHERE Amount = (SELECT MAX(Amount) FROM Bid) AND Ends > '2001-12-20 00:00:01';

SELECT COUNT(*) AS 'Number of Sellers whose Rating is Higher than 1000' FROM User WHERE Seller_Rating > 1000;

SELECT COUNT(DISTINCT UserID) AS 'Number of Users who are both Sellers and Bidders' FROM User, Item, Bid WHERE (UserID = SellerID AND SellerID = BidderID AND UserID = BidderID);

SELECT COUNT(DISTINCT Category) AS 'Number of Categories that include at least one item with a Bid of more than $100' FROM Item_Category
WHERE ItemID IN (SELECT ItemID FROM Bid WHERE Amount > 100.00);