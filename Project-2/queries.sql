SELECT COUNT(*) AS 'Number of Users' FROM User;

SELECT COUNT(*) AS 'Number of Items From New York' FROM Item WHERE BINARY Location = 'New York';

SELECT COUNT(ItemID) AS 'Number of Auctions Belonging to Exactly Four Categories' FROM (SELECT ItemID FROM Item_Category GROUP BY ItemID HAVING COUNT(Category) = 4)S;

--Still need 4--

SELECT COUNT(*) AS 'Number of Sellers whose Rating is Higher than 1000' FROM User WHERE Seller_Rating > 1000;

SELECT COUNT(DISTINCT UserID) AS 'Number of Users who are both Sellers and Bidders' FROM User, Item, Bid WHERE (UserID = SellerID AND SellerID = BidderID AND UserID = BidderID);

SELECT COUNT(DISTINCT Category) AS 'Number of Categories that include at least one item with a Bid of more than $100' FROM Item_Category
WHERE ItemID IN (SELECT ItemID FROM Bid WHERE Amount > 100.00);