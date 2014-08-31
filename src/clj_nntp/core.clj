(ns clj-nntp.core
  (:import (org.apache.commons.net.nntp NNTPClient
                                        ReplyIterator
                                        NewsgroupInfo
                                        SimpleNNTPHeader)))

(defn- connect-and-authenticate ^NNTPClient [server]
  (let [client ^NNTPClient (NNTPClient.)
        hostname (:hostname server)
        username (:username server)
        password (:password server)]
    (.connect client hostname)
    (if (and (seq username) (seq password))
      (.authenticate client username password))
    client))

(defn newsgroups [server]
  (let [client (connect-and-authenticate server)
        groups (doall (map #(.getNewsgroup %) (.iterateNewsgroups client)))]
    (.logout client)
    (.disconnect client)
    groups))

(defn post-article [server article]
  (let [header ^SimpleNNTPHeader (SimpleNNTPHeader. (:from article) (:subject article))
        client (connect-and-authenticate server)]
    (.addNewsgroup header (:newsgroup article))
    (.addHeaderField header "Organization" (:organization article))
    (if (.isAllowedToPost client)
      (let [writer (.postArticle client)]
        (.write writer (.toString header))
        (.write writer (:body article))
        (.close writer)))
    (let [return-val (if (.completePendingCommand client) true false)]
      (.logout client)
      (.disconnect client)
      return-val)))
