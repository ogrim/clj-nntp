(ns clj-nntp.core
  (:import (org.apache.commons.net.nntp NNTPClient
                                        ReplyIterator
                                        NewsgroupInfo
                                        SimpleNNTPHeader)))

(defn connect-and-authenticate ^NNTPClient [server]
  (let [client ^NNTPClient (NNTPClient.)
        hostname (:hostname server)
        username (:username server)
        password (:password server)]
    (.connect client hostname)
    (if (and (seq username) (seq password))
      (.authenticate client username password))
    client))

(defmacro with-connection
  [[varname server] & body]
  `(let [^NNTPClient ~varname (connect-and-authenticate ~server)
         result# ~@body]
     (.logout ~varname)
     (.disconnect ~varname)
     result#))

(defn newsgroups [server]
  (with-connection [client server]
    (doall (map #(.getNewsgroup ^NewsgroupInfo %) (.iterateNewsgroups client)))))

(defn post-article [server article]
  (let [header (SimpleNNTPHeader. (:from article) (:subject article))
        client (connect-and-authenticate server)]
    (.addNewsgroup header (:newsgroup article))
    (.addHeaderField header "Organization" (:organization article))
    (if (.isAllowedToPost client)
      (let [writer (.postArticle client)]
        (.write writer ^String (.toString header))
        (.write writer ^String (:body article))
        (.close writer)))
    (let [return-val (if (.completePendingCommand client) true false)]
      (.logout client)
      (.disconnect client)
      return-val)))

(defn post-article-2 [server article]
  (with-connection [client server]
    (let [header (SimpleNNTPHeader. (:from article) (:subject article))]
      (.addNewsgroup header (:newsgroup article))
      (.addHeaderField header "Organization" (:organization article))
      (if (.isAllowedToPost client)
        (let [writer (.postArticle client)]
          (if writer
            (do (.write writer ^String (.toString header))
                (.write writer ^String (:body article))
                (.close writer)
                (if (.completePendingCommand client) true false))))))))
