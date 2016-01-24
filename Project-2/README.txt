1. 
Item(ItemID (PRIMARY KEY), Currently, Buy_Price, First_Bid, Number_of_Bids, Location, Longitude, Latitude, Country, Started, Ends, SellerID (FOREIGN KEY), Description)
Bid(BidderID (PRIMARY KEY), Time (PRIMARY KEY), Amount, ItemID (PRIMARY KEY))
User(UserID (PRIMARY KEY), Location, Country, Seller_Rating, Bidder_Rating)
Category((ItemID, Category) (PRIMARY KEY))

2.
The nontrivial functional dependencies specify the keys in the relations.

3.
Yes. They all have keys on the LHS of the functional dependencies.

4. 
Yes. They do not contain more than one multivalued dependency.