#!/bin/bash

mysql CS144 < drop.sql

mysql CS144 < create.sql

ant
ant run-all

mysql CS144 < load.sql

rm User.dat
rm Bid.dat
rm Category.dat
rm Item.dat