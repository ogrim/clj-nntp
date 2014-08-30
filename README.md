# clj-nntp

A Clojure NNTP library wrapping Apache Commons Net NNTP.

Still only bare bones, more or less a proof-of-concept. Currently I only need to post articles to my private server, for consumption by gnus. Therefore the efficacy of this library may be limited.

## Usage

Many of the functions in clj-nntp takes a server specification as a map.

    (def server
      {:hostname "10.0.0.69"
      :port 119
      :username "ogrim"
      :password "1234"})


What newsgroups are on the server?

      (newsgroups server)
      =>
      ("control"
       "control.cancel"
       "control.checkgroups"
       "control.newgroup"
       "control.rmgroup"
       "junk"
       "local.general"
       "local.test"
       "clj.clj-nntp.dev")

You can post an article to a group. Specify the article like this:

    (def article
      {:from "gul.dukat@example.com"
      :subject "That space station"
      :body "It is mine, please go away."
      :newsgroup "clj.clj-nntp.dev"
      :organization "Cardassian Union"})

And post it like so, it will return true|false:

    (post-article server article)

And now I can read my article in gnus (or any other client of your preference)

![Alt text](./resources/gnus.jpg?raw=true "Article posted with clj-nntp in gnus")


## License

Copyright © 2014 Aleksander Skjæveland Larsen

Distributed under the Apache License, Version 2.0, the same as Apache Commons Net.
