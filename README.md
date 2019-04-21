# evolve the spacecraft's landing path on complex land figure
GA project for INFO6205

Our Problem describles how spacecrafts making successful landing on a complex land figure.

![alt text](6205project/image_meitu_1.jpg)

The picture above is the land shape designed by us, using Java Graphics2D and Canvas. Our first generation of spacecrafts(or called landers) starts fly from the start point with an initial power, and its speed & flying direction angle are generated randomly. Due to the gravity, after first flying most of them could have crashed into ground, their fly routes may looks like this:

![alt text](6205project/image (1).png)

The best landers are selected and used to create new landers.The problem is solved by using optimum speeds for maximizing the fuel efficiency using GA due to which eventually the landers slowly get better and start landing in the safe zone.
