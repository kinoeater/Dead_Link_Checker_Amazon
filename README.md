# Dead_Link_Checker_Amazon
Write a crawler that opens up the “Shop By Department” dropdown menu on the
amazon website, obtains a list of all department links and visits them to make
sure that there are no dead links.
Your crawler should keep a list of visited links in a text file in the form (link, page
title, status) , where status can be “OK” or “Dead link”.
After finishing, the crawler should name the file <timestamp>_results.txt and
automatically upload it to the /results folder in the DropBox
