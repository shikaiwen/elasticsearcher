
POST http://localhost:9200/people/_analyze
{
  "text" : "タイガー TIGER JBH-G101 [マイコン炊飯器 5.5合炊き 炊きたて ホワイト",
"tokenizer" : "kuromoji_tokenizer",
  "explain" : true,
  "attributes" : ["keyword"],
          "filter": [
            "kuromoji_baseform",
            "kuromoji_part_of_speech",
            "ja_stop",
            "kuromoji_number",
            "kuromoji_stemmer"
          ]

}


http://localhost:9200/_search
{"query":{"match":{"proDesc":"海外"}}}


https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-standard-analyzer.html#analysis-standard-analyzer

highlight
https://stackoverflow.com/questions/26945217/find-actual-matching-word-when-using-fuzzy-query-in-elastic-search

https://www.elastic.co/guide/en/elasticsearch/guide/current/mapping-intro.html#custom-field-mappings


set the return dataset size, default is 100
{
    "query": {
        "match": {
            "proDesc": "革靴"
        }
    },
"from":0,
    "size":100
}