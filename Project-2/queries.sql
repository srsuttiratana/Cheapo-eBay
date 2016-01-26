SELECT COUNT(*) FROM User;

SELECT COUNT(*) FROM Item WHERE Location = 'New York';

SELECT COUNT(*) FROM Category GROUP BY ItemID, Category HAVING COUNT(*) = 4;



SELECT COUNT(*) FROM User WHERE Seller_Rating > 1000;

SELECT COUNT(*) FROM User WHERE (Seller_Rating != NULL AND Bidder_Rating != NULL);

SELECT COUNT(*) FROM Bid, Category WHERE Amount > 100;