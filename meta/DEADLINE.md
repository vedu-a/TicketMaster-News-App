# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice looking HTML.

## Part 1: App Description

> Please provide a firendly description of your app, including the
> the primary functions available to users of the app. Be sure to
> describe exactly what APIs you are using and how they are connected
> in a meaningful way.

> **Also, include the GitHub `https` URL to your repository.**

url : https://github.com/vedu-a/cs1302-api

My app integrates the TicketMaster API awell as NewsAPI in order to fetch
events at a specified city and provide news about the a specific event. My
app first asks for a city, after that it dierects you to press the get events
button. The get events button then uses the TicketMaster API to fetch five
events happening at the city, displayed on the left of the screen. All
five eventsdisplayed  are clickable. Clicking an event will then use the
NewsAPI to fetch a news article about the event. The right side of the app
will then display the title of the article, a short description of the article,
and the url link to the article. If the user is interested, then they may copy
and paste into a web brower if they want to read further.

## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

Somehing I learnt that was new from working on this was the use of the
@SerializableName annotation. I was able to access Json objects that had
underscores and was able to name that whatever I wanted instead of being
constricted to sticking to the name fo the Json object in the response.


## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

If I were to start this project from scratch then I would take more
time researching more styling options for JavaFx. I feel I did an ok
job styling my app, but there is still definitly more I could do to
make the app look better.
