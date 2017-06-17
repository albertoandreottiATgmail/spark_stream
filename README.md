# spark_stream 
Teach yourself Spark Streaming in 10 lines of code!

In this small example I put together a minimalistic demo that showcases the very basic 
of Spark Streaming. 

## How does it work ?
It's very simple, in this example we're going to be reading JSON files from your local disk, 
src/main/scala/resources/people1.json
src/main/scala/resources/people2.json
We're going to perform a simple operation with a "Streaming Query", which is a central concept
of Spark Streaming; a query taking place on a table that grows dynamically.
We're going to run this query for two minutes, so the first thing you will see when running this
code will be the following,
`
+----------+---------+
|      name|birthyear|
+----------+---------+
|     Maxim|     1995|
|Aleksander|     1994|
|      Paul|     1973|
+----------+---------+
`

this is the content in people1.json. This is the first file the query found in the folder where the
query is scanning for files. You see that the query took effect because you see birth years instead
of ages.
Now let's go for the real fun, let's give this query more data; for this what we're goin to do is to
copy another of our JSON files to the folder that is being scanned,

~/spark_stream$ cp src/main/resources/people2.json target/scala-2.11/classes/

If you did this within the two minutes limit timeframe we set the query to run, you'll see this outpout,
`
+-------+---------+
|   name|birthyear|
+-------+---------+
|   Juan|     1995|
| Giulio|     1994|
|Rudolph|     1973|
+-------+---------+
`
these are the other people in our second JSON file, and you can see that the transformation took place on
them as well.
Enjoy!.
