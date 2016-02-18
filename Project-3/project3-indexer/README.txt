Decide which index(es) to create on which attribute(s). 
Document your design choices and briefly discuss why you've chosen to create your 
particular index(es).

====================================================================================
We decided to create an index on ItemID, Name, Category, Description and Content (which is Name,
Category, Description concatenated together), so that we could have an efficient keyword search
 (on item's name, category, and description). But, the item id and item name are the only attributes
 stored.
We also built a spatial index on longitude and latitude, as instructed.
